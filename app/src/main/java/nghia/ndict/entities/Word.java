package nghia.ndict.entities;

/**
 * Created with IntelliJ IDEA.
 * User: nghia
 * Date: 4/16/13
 * Time: 12:16 PM
 * To change this template use File | Settings | File Templates.
 */
public class Word extends BaseEntity {
    private String content;
    private int startMeaningIndex;
    private int endMeaningIndex;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getStartMeaningIndex() {
        return startMeaningIndex;
    }

    public void setStartMeaningIndex(int startMeaningIndex) {
        this.startMeaningIndex = startMeaningIndex;
    }

    public int getEndMeaningIndex() {
        return endMeaningIndex;
    }

    public void setEndMeaningIndex(int endMeaningIndex) {
        this.endMeaningIndex = endMeaningIndex;
    }
}
