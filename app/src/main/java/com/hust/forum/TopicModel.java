package com.hust.forum;

/**
 * Created by Administrator on 4/20/2016.
 */
public class TopicModel {
    private  String Title;
    private  int PostNum;
    private  int MemberNum;
    private  String UrlLink;

    public TopicModel() {
    }

    public TopicModel(String urlLink, int memberNum, int postNum, String title) {
        UrlLink = urlLink;
        MemberNum = memberNum;
        PostNum = postNum;
        Title = title;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public int getPostNum() {
        return PostNum;
    }

    public void setPostNum(int postNum) {
        PostNum = postNum;
    }

    public int getMemberNum() {
        return MemberNum;
    }

    public void setMemberNum(int memberNum) {
        MemberNum = memberNum;
    }

    public String getUrlLink() {
        return UrlLink;
    }

    public void setUrlLink(String urlLink) {
        UrlLink = urlLink;
    }
}
