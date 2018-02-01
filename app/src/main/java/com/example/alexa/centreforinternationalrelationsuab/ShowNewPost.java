package com.example.alexa.centreforinternationalrelationsuab;

@SuppressWarnings("ALL")
class ShowNewPost {
    private String Image_URL, Post_Title, Post_Content; // this fields must be same as Database fields

    public ShowNewPost(String image_URL, String image_Title, String post_Content) {
        Image_URL = image_URL;
        Post_Title = image_Title;
        Post_Content = post_Content;
    }

    public ShowNewPost() {
        //Require a empty constructor
    }

    public String getImage_URL() {
        return Image_URL;
    }
    public void setImage_URL(String image_URL) {
        Image_URL = image_URL;
    }

    public String getPost_Title() {
        return Post_Title;
    }

    public void setPost_Title(String title) {
        Post_Title = title;
    }

    public String getPost_Content() {
        return Post_Content;
    }

    public void setPost_Content(String title) {
        Post_Content = title;
    }
}
