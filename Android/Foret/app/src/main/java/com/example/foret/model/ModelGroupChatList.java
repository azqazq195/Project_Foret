package com.example.foret.model;

public class ModelGroupChatList {

    String GroupName,GroupPhoto,GroupLeader,GroupDescription,
            GroupId,GroupMaxMember,GroupCurrentJoinedMember,Group_date_issued;


    public ModelGroupChatList() {
    }

    public ModelGroupChatList(String groupName, String groupPhoto, String groupLeader,
                              String groupDescription, String groupId, String groupMaxMember,
                              String groupCurrentJoinedMember, String group_date_issued) {
        GroupName = groupName;
        GroupPhoto = groupPhoto;
        GroupLeader = groupLeader;
        GroupDescription = groupDescription;
        GroupId = groupId;
        GroupMaxMember = groupMaxMember;
        GroupCurrentJoinedMember = groupCurrentJoinedMember;
        Group_date_issued = group_date_issued;
    }

    public String getGroupName() {
        return GroupName;
    }

    public void setGroupName(String groupName) {
        GroupName = groupName;
    }

    public String getGroupPhoto() {
        return GroupPhoto;
    }

    public void setGroupPhoto(String groupPhoto) {
        GroupPhoto = groupPhoto;
    }

    public String getGroupLeader() {
        return GroupLeader;
    }

    public void setGroupLeader(String groupLeader) {
        GroupLeader = groupLeader;
    }

    public String getGroupDescription() {
        return GroupDescription;
    }

    public void setGroupDescription(String groupDescription) {
        GroupDescription = groupDescription;
    }

    public String getGroupId() {
        return GroupId;
    }

    public void setGroupId(String groupId) {
        GroupId = groupId;
    }

    public String getGroupMaxMember() {
        return GroupMaxMember;
    }

    public void setGroupMaxMember(String groupMaxMember) {
        GroupMaxMember = groupMaxMember;
    }

    public String getGroupCurrentJoinedMember() {
        return GroupCurrentJoinedMember;
    }

    public void setGroupCurrentJoinedMember(String groupCurrentJoinedMember) {
        GroupCurrentJoinedMember = groupCurrentJoinedMember;
    }

    public String getGroup_date_issued() {
        return Group_date_issued;
    }

    public void setGroup_date_issued(String group_date_issued) {
        Group_date_issued = group_date_issued;
    }
}
