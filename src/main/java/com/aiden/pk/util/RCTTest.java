package com.aiden.pk.util;

import com.moilioncircle.redis.replicator.Configuration;
import com.moilioncircle.redis.replicator.FileType;
import com.moilioncircle.redis.replicator.RedisReplicator;
import com.moilioncircle.redis.replicator.Replicator;
import com.moilioncircle.redis.replicator.event.Event;
import com.moilioncircle.redis.replicator.event.EventListener;
import com.moilioncircle.redis.replicator.rdb.datatype.AuxField;
import com.moilioncircle.redis.replicator.rdb.datatype.KeyStringValueString;
import com.moilioncircle.redis.replicator.rdb.datatype.KeyStringValueZSet;
import com.moilioncircle.redis.replicator.rdb.datatype.KeyValuePair;
import com.moilioncircle.redis.replicator.rdb.dump.DumpRdbVisitor;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class RCTTest {

    public static void main(String[] args) throws Exception {
        readInputStream();
    }
    public static void readInputStream() throws IOException {
        final Replicator replicator = new RedisReplicator(new File("H:\\9002\\dump.rdb"), FileType.RDB,
                Configuration.defaultSetting());

        PrefixKeyTrie prefixKeyTrie = new PrefixKeyTrie();

        final Set<Object> set = new HashSet<Object>();
        replicator.addEventListener(new EventListener() {
            @Override
            public void onEvent(Replicator repl, Event event) {
                set.add(event.getClass());
                if (event instanceof AuxField) {
                    AuxField auxField = (AuxField) event;
                    if ("used-mem".equals(auxField.getAuxKey())){
                        System.out.println("aux-> "+auxField.getAuxKey()+":"+auxField.getAuxValue());
                    }
                }
                if (event instanceof KeyStringValueZSet) {
                    KeyStringValueZSet kv = (KeyStringValueZSet) event;
                    String key = new String(kv.getKey());
                    Set value = kv.getValue();
                    System.out.println(key + " value: "+value);
//                    char firstChar = key.charAt(0);
//                    if(firstChar < '0' || firstChar > '9'){
//                        prefixKeyTrie.insert(key);
//                    }
                }
            }

        });
        replicator.open();
        //List<String> maxPrefix = prefixKeyTrie.findMaxPrefix(3000);
        set.forEach(System.out::println);
    }
}
