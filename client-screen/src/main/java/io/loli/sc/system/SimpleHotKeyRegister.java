package io.loli.sc.system;

import java.util.ArrayList;
import java.util.List;

import javax.swing.KeyStroke;

import com.tulskiy.keymaster.common.Provider;

interface SimpleHotKeyListener {
    void onHotKey(int index);
}

class SimpleHotKeyProvider {
    private static SimpleHotKeyProvider shp;

    private SimpleHotKeyProvider() {
    }

    public static SimpleHotKeyProvider getInstance() {
        if (shp == null) {
            shp = new SimpleHotKeyProvider();
        }
        return shp;
    }

    private static Provider provider = Provider.getCurrentProvider(true);
    private List<Integer[]> hotKeyList = new ArrayList<>();

    public void registerHotKey(int index, int mask, int key) {
        Integer[] hotKey = new Integer[] { index, mask, key };
        hotKeyList.add(hotKey);
    }

    public void addHotKeyListener(SimpleHotKeyListener listener) {
        hotKeyList.forEach(hotKey -> provider.register(
                KeyStroke.getKeyStroke(hotKey[2], hotKey[1]), key -> {
                    listener.onHotKey(hotKey[0]);
                }));
    }

    public void stop() {
        provider.reset();
        provider.stop();
    }
}

public class SimpleHotKeyRegister implements HotKeyRegister{
    private HotKeyTask task;

    public SimpleHotKeyRegister(HotKeyTask task){
        this.task = task;
    }
    private SimpleHotKeyProvider getHotKey(){
        return SimpleHotKeyProvider.getInstance();
    }
    @Override
    public boolean canRegister(int index, int mask, int key) {
        return true;
    }

    @Override
    public void register(int index, int mask, int key) {
        getHotKey().registerHotKey(index, mask, key);
    }

    @Override
    public void stop() {
        getHotKey().stop();
    }

    @Override
    public void startListen() {
        getHotKey().addHotKeyListener(index->{
            task.run(index);
        });
    }
}
