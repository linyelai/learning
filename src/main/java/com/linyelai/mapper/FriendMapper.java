package com.linyelai.mapper;

import com.linyelai.po.FriendPO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author: linyelai
 * date: 2025/8/5
 * description: 好友mapper
 */
public interface FriendMapper {

    int addFriend(FriendPO friendPO);

    List<FriendPO> findByUserId(Long userId);

    FriendPO findByUserIdAndFriend(@Param("userId") Long userId,@Param("friendId") Long friendId);

    void deleteFriend(@Param("userId") Long userId,@Param("friendId") Long friendId);
}
