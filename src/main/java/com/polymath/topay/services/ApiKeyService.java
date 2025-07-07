package com.polymath.topay.services;

import com.polymath.topay.exceptions.CustomNotAuthorized;
import com.polymath.topay.exceptions.CustomNotFound;
import com.polymath.topay.models.ApiKey;
import com.polymath.topay.models.Merchants;
import com.polymath.topay.repositories.ApiKeyRepository;
import com.polymath.topay.repositories.MerchantsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ApiKeyService {
    private final ApiKeyRepository apiKeyRepository;
    private final MerchantsRepository merchantsRepository;
    private final SecureRandom secureRandom = new SecureRandom();

    public Merchants validateApiKey(String apiKey){
        ApiKey key = apiKeyRepository.findByPublicKeyOrSecretKey(apiKey,apiKey).orElseThrow(()->new CustomNotAuthorized("Invalid api key"));
        if(!key.isActive()){
            throw new CustomNotAuthorized("Api key is not active");
        }
        key.setLastUsedAt(LocalDateTime.now());
        apiKeyRepository.save(key);
        return merchantsRepository.findByApiKeyId(key.getId())
                .orElseThrow(() -> new CustomNotFound("Merchant not found for this API key"));
    }

    public void generateApiKeyForMerchant(UUID merchantId){
        Merchants merchants = merchantsRepository.findById(merchantId).orElseThrow(()->new CustomNotFound("Merchant not found"));
        ApiKey testApiKey = generateApiKey(ApiKey.Environment.TEST);
        testApiKey = apiKeyRepository.save(testApiKey);
        merchants.setTestApiKey(testApiKey);
        ApiKey liveApiKey = generateApiKey(ApiKey.Environment.LIVE);
        liveApiKey = apiKeyRepository.save(liveApiKey);
        merchants.setLiveApiKey(liveApiKey);
        merchantsRepository.save(merchants);
        if(merchants.getTestApiKey()==null || merchants.getLiveApiKey()==null){
            throw new CustomNotFound("Api key generation failed");
        }
    }

    public void regenerateApiKeys(UUID merchantId,ApiKey.Environment environment){
        Merchants merchants = merchantsRepository.findById(merchantId).orElseThrow(()->new CustomNotFound("Merchant not found"));
        ApiKey newApiKey = generateApiKey(environment);
        newApiKey = apiKeyRepository.save(newApiKey);
        if(environment==ApiKey.Environment.TEST){
            if(merchants.getTestApiKey()!=null){
                merchants.getTestApiKey().setActive(false);
                apiKeyRepository.save(merchants.getTestApiKey());
            }
            merchants.setTestApiKey(newApiKey);
        }else {
            if(merchants.getLiveApiKey()!=null){
                merchants.getLiveApiKey().setActive(false);
                apiKeyRepository.save(merchants.getLiveApiKey());
            }
            merchants.setLiveApiKey(newApiKey);
        }
        merchantsRepository.save(merchants);
    }

    private ApiKey generateApiKey(ApiKey.Environment environment){
        String prefix = environment==ApiKey.Environment.TEST?"test":"live";
        ApiKey apiKey = new ApiKey();
        apiKey.setPublicKey(generateKey("pk_"+prefix+"_"));
        apiKey.setSecretKey(generateKey("sk_"+prefix+"_"));
        apiKey.setEnvironment(environment);
        apiKey.setActive(true);
        return apiKey;
    }

    private String generateKey(String prefix){
        byte[] bytes = new byte[32];
        secureRandom.nextBytes(bytes);
        return prefix+ Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
    }
}
