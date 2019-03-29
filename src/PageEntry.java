public class PageEntry {

    private int variableID;
    private int pageNumber;
    private int lastAccess;

    public PageEntry(int id, int pg, int acc){
        this.variableID = id;
        this.pageNumber = pg;
        this.lastAccess = acc;
    }

    public void setLastAccess(int time){
        this.lastAccess = time;
    }

    public int getVariableID() {
        return variableID;
    }

    public int getPageNumber() {
        return pageNumber;
    }

    public int getLastAccess() {
        return lastAccess;
    }
}
