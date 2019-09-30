package cw.bean;

/**   
 * @ClassName:  WordWithCount   
 * @Description:TODO(这里用一句话描述这个类的作用)
 */
public class WordWithCount {
	public String word;
    public long count;

    public WordWithCount() {}

    public WordWithCount(String word, long count) {
        this.word = word;
        this.count = count;
    }

    @Override
    public String toString() {
        return word + " : " + count;
    }
}
