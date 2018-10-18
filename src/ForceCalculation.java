import java.io.*;
import java.text.DecimalFormat;
import java.util.*;

public class ForceCalculation {

    private static final Map<Integer, List<List<Integer>>> sRatingMap = new LinkedHashMap<>();

    public static void main(String[] args) throws IOException {
        if (args.length != 1) {
            System.out.println("Please input the src file path...");
        }

        String srcPath = args[0];
        FileInputStream fstream = new FileInputStream(srcPath);
        BufferedReader br = new BufferedReader(new InputStreamReader(fstream));

        /**
         * Format:
         * 1,2,3.5,1112486027
         * 1,29,3.5,1112484676
         */
        String line;
        boolean isFirstLine = true;
        while ((line = br.readLine()) != null)   {
            if (isFirstLine) {
                isFirstLine = false;
                continue;
            }

            if (line.isEmpty()) {
                continue;
            }

            String[] tokens = line.split(",");
            if (tokens.length != 4) {
                continue;
            }

            float rateScore = Float.valueOf(tokens[2]);
            if (rateScore < 0.5f || rateScore > 5.0f) {
                continue;
            }

            int userId = Integer.valueOf(tokens[0]);
            int movieId = Integer.valueOf(tokens[1]);
            boolean like = rateScore > 2.5f;
            if (!sRatingMap.containsKey(userId)) {
                List<List<Integer>> ratingList = new ArrayList<>();
                List<Integer> likeRating = new ArrayList<>();
                List<Integer> dislikeRating = new ArrayList<>();
                ratingList.add(likeRating);
                ratingList.add(dislikeRating);
                sRatingMap.put(userId, ratingList);

                // total 138493 users
                if (sRatingMap.size() % 10000 == 0) {
                    System.out.println("Already read:" + sRatingMap.size() + "/138493");
                }
            }

            if (like) {
                sRatingMap.get(userId).get(0).add(movieId);
            } else {
                sRatingMap.get(userId).get(1).add(movieId);
            }
        }
        br.close();

        System.out.println("Go to top accounting stage...");
        TreeSet<UserPair> topUserPair = new TreeSet<>(new Comparator<UserPair>() {
            @Override
            public int compare(UserPair o1, UserPair o2) {
                return o1.jaccard > o2.jaccard ? -1 :(o1.jaccard < o2.jaccard ? 1 : 0);
            }
        });

        List<Integer> userIds = new ArrayList<>(sRatingMap.keySet());
        for (int curIndex = 0; curIndex < userIds.size(); curIndex++) {
            if (curIndex % 10000 == 0) {
                System.out.println("Already calculate:" + curIndex + "/" + userIds.size());
            }

            for (int otherIndex = curIndex + 1; otherIndex < userIds.size(); otherIndex++) {
                int curUserId = userIds.get(curIndex);
                int otherUserId = userIds.get(otherIndex);

                long commonLikeCount = commonCount(sRatingMap.get(curUserId).get(0), sRatingMap.get(otherUserId).get(0));
                long commonDislikeCount = commonCount(sRatingMap.get(curUserId).get(1), sRatingMap.get(otherUserId).get(1));

                long commonCount = commonLikeCount + commonDislikeCount;
                long base = sRatingMap.get(curUserId).get(0).size() + sRatingMap.get(curUserId).get(1).size()
                        + sRatingMap.get(otherUserId).get(0).size() + sRatingMap.get(otherUserId).get(1).size();
                double jaccard = (double) commonCount / (base - commonCount);
                UserPair pair = new UserPair(curUserId, otherUserId, jaccard);
                if (topUserPair.size() < 100) {
                    topUserPair.add(pair);
                } else {
                    if (jaccard > topUserPair.last().jaccard) {
                        topUserPair.pollLast();
                        topUserPair.add(pair);
                    }
                }
            }
        }

        System.out.println("Start to write output file...");

        String dst = "./output.txt";
        File fout = new File(dst);
        FileOutputStream fos = new FileOutputStream(fout);
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos));
        DecimalFormat df = new DecimalFormat("#.###");
        for (UserPair pair : topUserPair) {
            String outputLine = "\"" + pair.curUserId + "\"\t\"" + pair.otherUserId + "\"\t" + df.format(pair.jaccard);
            bw.write(outputLine);
            bw.newLine();
        }
        bw.close();

        System.out.println("Finished!");
    }

    public static int commonCount(List<Integer> curList, List<Integer> otherList) {
        int res = 0;
        int curIndex = 0, otherIndex = 0;
        while (curIndex < curList.size() && otherIndex < otherList.size()) {
            if (curList.get(curIndex).equals(otherList.get(otherIndex))) {
                res++;
                curIndex++;
                otherIndex++;
            } else if (curList.get(curIndex) > otherList.get(otherIndex)) {
                otherIndex++;
            } else {
                curIndex++;
            }
        }
        return res;
    }

}
