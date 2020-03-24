package com.android.ocat.global.entity;

/**
 * User
 */
public class User {
        private Integer id;
        private String username;
        private String password;
        private String email;
        private String phone;
        private String img;
        private String question;
        private String answer;
        private Integer role;
        private Integer status;
        private String createTime;
        private String updateTime;

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username == null ? null : username.trim();
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password == null ? null : password.trim();
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email == null ? null : email.trim();
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone == null ? null : phone.trim();
        }

        public String getImg() {
            return img;
        }

        public void setImg(String img) {
            this.img = img == null ? null : img.trim();
        }

        public String getQuestion() {
            return question;
        }

        public void setQuestion(String question) {
            this.question = question == null ? null : question.trim();
        }

        public String getAnswer() {
            return answer;
        }

        public void setAnswer(String answer) {
            this.answer = answer == null ? null : answer.trim();
        }

        public Integer getRole() {
            return role;
        }

        public void setRole(Integer role) {
            this.role = role;
        }

        public Integer getStatus() {
            return status;
        }

        public void setStatus(Integer status) {
            this.status = status;
        }

        public String getCreateTime() {
            return createTime;
        }

        public void setCreateTime(String createTime) {
            this.createTime = createTime;
        }

        public String getUpdateTime() {
            return updateTime;
        }

        public void setUpdateTime(String updateTime) {
            this.updateTime = updateTime;
        }

        @Override
        public String toString() {
            return "User{" +
                    "id=" + id +
                    ", username='" + username + '\'' +
                    ", password='" + password + '\'' +
                    ", email='" + email + '\'' +
                    ", phone='" + phone + '\'' +
                    ", img='" + img + '\'' +
                    ", question='" + question + '\'' +
                    ", answer='" + answer + '\'' +
                    ", role=" + role +
                    ", status=" + status +
                    ", createTime='" + createTime + '\'' +
                    ", updateTime='" + updateTime + '\'' +
                    '}';
        }
}
