public class UserPair {

    int curUserId;
    int otherUserId;
    double jaccard;

    public UserPair(int curUserId, int otherUserId, double jaccard) {
        this.curUserId = curUserId;
        this.otherUserId = otherUserId;
        this.jaccard = jaccard;
    }
}
