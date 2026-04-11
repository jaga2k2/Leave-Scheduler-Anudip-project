package com.kt.leave.dto;

public class AuthDtos {

    // ===== LOGIN REQUEST =====
    public static class LoginRequest {

        private String username;
        private String password;

        public LoginRequest() {
        }

        public LoginRequest(String username, String password) {
            this.username = username;
            this.password = password;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }
    }

    // ===== LOGIN RESPONSE =====
    public static class LoginResponse {

        private String token;
        private String username;
        private String role;
        private Long userId;
        private String fullName;

        public LoginResponse() {
        }

        public LoginResponse(String token, String username, String role, Long userId, String fullName) {
            this.token = token;
            this.username = username;
            this.role = role;
            this.userId = userId;
            this.fullName = fullName;
        }

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getRole() {
            return role;
        }

        public void setRole(String role) {
            this.role = role;
        }

        public Long getUserId() {
            return userId;
        }

        public void setUserId(Long userId) {
            this.userId = userId;
        }

        public String getFullName() {
            return fullName;
        }

        public void setFullName(String fullName) {
            this.fullName = fullName;
        }
    }

    // ===== CHANGE PASSWORD REQUEST =====
    public static class ChangePasswordRequest {

        private String oldPassword;
        private String newPassword;

        public ChangePasswordRequest() {
        }

        public ChangePasswordRequest(String oldPassword, String newPassword) {
            this.oldPassword = oldPassword;
            this.newPassword = newPassword;
        }

        public String getOldPassword() {
            return oldPassword;
        }

        public void setOldPassword(String oldPassword) {
            this.oldPassword = oldPassword;
        }

        public String getNewPassword() {
            return newPassword;
        }

        public void setNewPassword(String newPassword) {
            this.newPassword = newPassword;
        }
    }
}