package com.changjoo.ohyeah.model;

/**
 * 네이버 로그인 후 프로필정보를 받아올 그릇
 */

public class NaverProfileModel {
    private String resultcode;
    private String message;
    private Response response;

    public Response getResponse() {
        return response;
    }

    public void setResponse(Response response) {
        this.response = response;
    }

    public String getResultcode() {
        return resultcode;
    }

    public void setResultcode(String resultcode) {
        this.resultcode = resultcode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }


    public class Response{
        String nickname;
        String enc_id;
        String profile_image;
        String age;
        String gender;
        String id;
        String name;
        String email;
        String birthday;

        @Override
        public String toString() {
            return "Response{" +
                    "nickname='" + nickname + '\'' +
                    ", enc_id='" + enc_id + '\'' +
                    ", profile_image='" + profile_image + '\'' +
                    ", age='" + age + '\'' +
                    ", gender='" + gender + '\'' +
                    ", id='" + id + '\'' +
                    ", name='" + name + '\'' +
                    ", email='" + email + '\'' +
                    ", birthday='" + birthday + '\'' +
                    '}';
        }

        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }

        public String getEnc_id() {
            return enc_id;
        }

        public void setEnc_id(String enc_id) {
            this.enc_id = enc_id;
        }

        public String getProfile_image() {
            return profile_image;
        }

        public void setProfile_image(String profile_image) {
            this.profile_image = profile_image;
        }

        public String getAge() {
            return age;
        }

        public void setAge(String age) {
            this.age = age;
        }

        public String getGender() {
            return gender;
        }

        public void setGender(String gender) {
            this.gender = gender;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getBirthday() {
            return birthday;
        }

        public void setBirthday(String birthday) {
            this.birthday = birthday;
        }
    }
}
