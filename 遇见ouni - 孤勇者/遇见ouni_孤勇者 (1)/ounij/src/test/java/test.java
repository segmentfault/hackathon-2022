import java.util.*;

public class test {
    public static void main(String[] args) {
        generateParenthesis(3);
        System.out.println(ans);
    }
     public  static List<String> ans = new ArrayList<>();
    public static List<String> generateParenthesis(int n) {
        helper(new StringBuilder(),0,0,n);
        return ans;
    }
    public static void helper(StringBuilder buffer,int left_num, int right_num,int n){
        if(buffer.length() == 2*n && left_num == n && right_num == n){
            ans.add(buffer.toString());
            return;
        }
        if(left_num <= n){
            buffer.append("(");
            helper(buffer,left_num+1,right_num,n);
            buffer.deleteCharAt(buffer.length()-1);
        }
        if(left_num > right_num && right_num <= n){
            buffer.append(")");
            helper(buffer,left_num,right_num+1,n);
            buffer.deleteCharAt(buffer.length()-1);
        }
    }
}
