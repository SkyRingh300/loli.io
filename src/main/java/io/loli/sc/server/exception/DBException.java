package io.loli.sc.server.exception;

/**
 * 自定义数据库异常 <br/>
 * User: choco(loli@linux.com) <br/>
 * Date: 2014年3月25日 <br/>
 * Time: 下午8:59:47 <br/>
 * @author choco
 */
public class DBException extends RuntimeException {
    private static final long serialVersionUID = 5261631913377289542L;

    public DBException() {
        super();
    }

    public DBException(String msg) {
        super(msg);
    }

    public DBException(Exception exp) {
        super(exp);
    }
}
