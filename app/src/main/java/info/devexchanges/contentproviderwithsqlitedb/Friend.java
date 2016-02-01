package info.devexchanges.contentproviderwithsqlitedb;

public class Friend {

    private String name;
    private String job;

    public Friend(String name, String job) {
        this.name = name;
        this.job = job;
    }

    public String getName() {
        return name;
    }

    public String getJob() {
        return job;
    }
}
