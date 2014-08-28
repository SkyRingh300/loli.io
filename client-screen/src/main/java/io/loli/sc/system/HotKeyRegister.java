package io.loli.sc.system;

public interface HotKeyRegister {
    public boolean canRegister(int index, int mask, int key);

    public void register(int index, int mask, int key);

    public void stop();

    public void startListen();
}
