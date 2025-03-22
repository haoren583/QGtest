package DataBaseConnectPool.ORM;

@DataEntity(tableName = "test_table")
public class TestClass {
    @Id
    @Column(name = "id")
    private int ID;
    @Column(name = "name")
    private String NAME;
    @Column(name = "age")
    private int AGE;

    public TestClass(int ID, String NAME, int AGE) {
        this.ID = ID;
        this.NAME = NAME;
        this.AGE = AGE;
    }
}
