/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.svjis.bean;

/**
 *
 * @author berk
 */
public class ArticleListInfo {
    private int numOfArticles;
    private int pageSize;
    private int actualPage;
    private int menuNodeId;

    public int getNumOfPages() {
        int result = 0;
        if (pageSize != 0) {
            result = (numOfArticles / pageSize);
            if ((numOfArticles % pageSize) != 0) {
                result++;
            }
        }
        return result;
    }
    
    /**
     * @return the numOfArticles
     */
    public int getNumOfArticles() {
        return numOfArticles;
    }

    /**
     * @param numOfArticles the numOfArticles to set
     */
    public void setNumOfArticles(int numOfArticles) {
        this.numOfArticles = numOfArticles;
    }

    /**
     * @return the pageSize
     */
    public int getPageSize() {
        return pageSize;
    }

    /**
     * @param pageSize the pageSize to set
     */
    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    /**
     * @return the actualPage
     */
    public int getActualPage() {
        return actualPage;
    }

    /**
     * @param actualPage the actualPage to set
     */
    public void setActualPage(int actualPage) {
        this.actualPage = actualPage;
    }

    /**
     * @return the menuNodeId
     */
    public int getMenuNodeId() {
        return menuNodeId;
    }

    /**
     * @param menuNodeId the menuNodeId to set
     */
    public void setMenuNodeId(int menuNodeId) {
        this.menuNodeId = menuNodeId;
    }
}
