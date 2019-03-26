package net.bigmir;

public class Photo {
    private long id;
    private String name;
    private byte[] bytes;

    public Photo(long id, String name, byte[] bytes) {
        this.id = id;
        this.name = name;
        this.bytes = bytes;
    }

    public Photo() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public byte[] getBytes() {
        return bytes;
    }

    public void setBytes(byte[] bytes) {
        this.bytes = bytes;
    }
}
