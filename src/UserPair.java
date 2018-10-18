public class UserPair {

    int curUserId;
    int otherUserId;
    double jaccard;

    public UserPair(int curUserId, int otherUserId, double jaccard) {
        this.curUserId = curUserId;
        this.otherUserId = otherUserId;
        this.jaccard = jaccard;
    }

    @Override
    public int hashCode() {
        int res = 17;
        res = res * 31 + (int) Math.min(curUserId, otherUserId);
        res = res * 31 + (int) Math.max(curUserId, otherUserId);
        return res;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof UserPair) {
            UserPair otherPair = (UserPair) obj;
            return (this.curUserId == otherPair.curUserId
                    && this.otherUserId == otherPair.otherUserId)
                    || (this.curUserId == otherPair.otherUserId
                    && this.otherUserId == otherPair.curUserId);
        }
        return false;
    }

}
