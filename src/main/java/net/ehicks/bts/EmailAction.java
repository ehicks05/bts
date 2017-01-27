package net.ehicks.bts;

public enum EmailAction
{
    CREATE_ISSUE(1, "created an issue"),
    UPDATE_ISSUE(2, "updated an issue"),
    ADD_COMMENT(3, "created a comment"),
    EDIT_COMMENT(4, "edited a comment"),
    TEST(5, "sent a test email");

    private long id;
    private String verb = "";

    EmailAction(long id, String verb)
    {
        this.id = id;
        this.verb = verb;
    }

    public static EmailAction getById(long id)
    {
        for (EmailAction emailAction : EmailAction.values())
            if (id == emailAction.id)
                return emailAction;
        return null;
    }

    public long getId()
    {
        return id;
    }

    public void setId(long id)
    {
        this.id = id;
    }

    public String getVerb()
    {
        return verb;
    }

    public void setVerb(String verb)
    {
        this.verb = verb;
    }
}