package br.com.mili.milibackend.shared.exception;

public class MyException extends Exception
{
    private final String message;
    /**
     * @param message
     */
    public MyException(String message)
    {
        super(message);
        this.message = message;
    }

    /**
     * @param message
     * @param cause
     */
    public MyException(String message, Throwable cause)
    {
        super(message, cause);
        this.message = message;
    }


    public String getCause(Throwable cause)
    {
        if(cause == null )
            return null;

        String msg =  cause.getMessage();

        if(msg.contains("ORA-"))
            return msg;

        if(cause.getCause() != null)
            msg = getCause(cause.getCause());

        return msg;
    }

    @Override
    public String toString()
    {
        return message != null ? message : super.toString();
    }


    @Override
    public String getMessage() {
        return message;
    }
}
