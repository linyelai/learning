package com.linyelai.service.impl;

import com.linyelai.mapper.FriendMapper;
import com.linyelai.mapper.UserMapper;
import com.linyelai.po.FriendPO;
import com.linyelai.po.UserPO;
import com.linyelai.service.FriendService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author: linyelai
 * date: 2025/8/5
 * description: 好友管理
 */

@Service
public class FriendServiceImpl implements FriendService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private FriendMapper friendMapper;
    @Override
    @Transactional
    public boolean addFriend(Long friendId, Long userId) {
        //查看是不是已经存在了好友信息
        FriendPO friendPO = friendMapper.findByUserIdAndFriend(userId,friendId);
        if(friendPO!=null){
            return false;
        }
        //查看好友是否存在
       UserPO userPO =  userMapper.findById(friendId);
        if(userPO==null){
            return  false;
        }
        String friendName = userPO.getUsername();
        FriendPO friendPO1 = new FriendPO();
        friendPO1.setFriendId(friendId);
        friendPO1.setFriendName(friendName);
        friendPO1.setUserId(userId);
        LocalDateTime now = LocalDateTime.now();
        friendPO1.setCreate_time(now);
        friendPO1.setUpdate_time(now);
        friendMapper.addFriend(friendPO1);
        UserPO currentUserPO = userMapper.findById(userId);

        String friendName1 = currentUserPO.getUsername();
        FriendPO friendPO2 = new FriendPO();
        friendPO2.setFriendId(userId);
        friendPO2.setFriendName(friendName1);
        friendPO2.setUserId(friendId);
        friendPO2.setCreate_time(now);
        friendPO2.setUpdate_time(now);
        friendMapper.addFriend(friendPO2);
        return true;
    }

    /**
     * 查找好友列表
     * @param userId
     * @return
     */
    @Override
   public List<FriendPO> findFriend(Long userId){

        return friendMapper.findByUserId(userId);
    }

    @Override
    public boolean deleteFriend(Long userId, Long friendId) {
         friendMapper.deleteFriend(userId,friendId);
         return true;
    }
}
