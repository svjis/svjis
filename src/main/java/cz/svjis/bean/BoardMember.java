/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.svjis.bean;

/**
 *
 * @author jarberan
 */
public class BoardMember {
    private User user;
    private BoardMemberType boardMemberType;

    /**
     * @return the user
     */
    public User getUser() {
        return user;
    }

    /**
     * @param user the user to set
     */
    public void setUser(User user) {
        this.user = user;
    }

    /**
     * @return the boardMemberType
     */
    public BoardMemberType getBoardMemberType() {
        return boardMemberType;
    }

    /**
     * @param boardMemberType the boardMemberType to set
     */
    public void setBoardMemberType(BoardMemberType boardMemberType) {
        this.boardMemberType = boardMemberType;
    }
}
