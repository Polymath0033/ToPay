package com.polymath.topay.dtos.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class ApiResponse<T> {
    private int status;
    private String message;
    private T data;
    private ErrorDetails errorDetails;
    private LocalDateTime timestamp;
    private MetaData metaData;

    public ApiResponse(int status, String message, T data, LocalDateTime timestamp){
        this.status = status;
        this.message = message;
        this.data = data;
        this.timestamp = timestamp;
    }

    public ApiResponse(int status, String message, T data, ErrorDetails errorDetails, LocalDateTime timestamp){
        this.status = status;
        this.message = message;
        this.data = data;
        this.errorDetails = errorDetails;
        this.timestamp = timestamp;
    }

    @Data
    @AllArgsConstructor
    public static class ErrorDetails{
        private Object details;
    }

    @Data
    @AllArgsConstructor
    private static class MetaData{
        private PaginationInfo pagination;
    }

    @Data
    @AllArgsConstructor
    private static class PaginationInfo{
        private int page;
        private int limit;
        private int totalPages;
        private int currentPage;
        private int totals;
        private int totalItems;
        private int prevPage;
        private int nextPage;
        private int hasPrevPage;
        private int hasNextPage;

    }

    public static <T> ApiResponse<T> success(int status, String message, T data){
        return new ApiResponse<>(status,message,data,LocalDateTime.now());
    }

    public static <T> ApiResponse<T> error(int status,Object details, String message){
        return new ApiResponse<>(status,message,null,new ErrorDetails(details),LocalDateTime.now());
    }
}
