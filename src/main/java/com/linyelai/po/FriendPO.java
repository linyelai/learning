package com.linyelai.po;

/**
 * @author: linyelai
 * date: 2025/8/5
 * description: TODO
 */
public class FriendPO {
    private Long id;
    private Long userId;
    private Long friendId;

    private String friendName;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getFriendId() {
        return friendId;
    }

    public void setFriendId(Long friendId) {
        this.friendId = friendId;
    }

    public String getFriendName() {
        return friendName;
    }

    public void setFriendName(String friendName) {
        this.friendName = friendName;
    }
}
