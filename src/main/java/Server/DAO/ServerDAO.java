package Server.DAO;

/**
 * @author blue
 * @date 2023/1/15 19:05
 **/
public interface ServerDAO {

    /** 智能客服回答客服问题的方法
     * @param question 用户提的问题
     * @return 智能客服回复
     */
     String response(String question);
}
