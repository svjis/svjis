/*
 *       BoardMember.java
 *
 *       This file is part of SVJIS project.
 *       https://github.com/svjis/svjis
 *
 *       SVJIS is free software; you can redistribute it and/or modify
 *       it under the terms of the GNU General Public License as published by
 *       the Free Software Foundation; either version 3 of the License, or
 *       (at your option) any later version. <http://www.gnu.org/licenses/>
 */

package cz.svjis.bean;

/**
 *
 * @author jarberan
 */
public class BoardMember {
    private User user;
    private BoardMemberType boardMemberType;
    
    public BoardMember() {
        super();
        user = new User();
        boardMemberType = new BoardMemberType();
    }

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
