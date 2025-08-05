package com.linyelai.service;

import com.linyelai.po.FriendPO;

import java.util.List;

public interface FriendService {

    boolean addFriend(Long friendId,Long userId);
    List<FriendPO> findFriend(Long userId);

    /**
     * 删除好友
     * @param userId
     * @param friendId
     * @return
     */
    boolean deleteFriend(Long userId,Long friendId);
}
