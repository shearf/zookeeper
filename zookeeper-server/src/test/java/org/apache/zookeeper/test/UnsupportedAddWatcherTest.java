/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.zookeeper.test;

import org.apache.zookeeper.AddWatchMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.server.watch.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collections;

public class UnsupportedAddWatcherTest extends ClientBase {

    public static class StubbedWatchManager implements IWatchManager {
        @Override
        public boolean addWatch(String path, Watcher watcher) {
            return false;
        }

        @Override
        public boolean containsWatcher(String path, Watcher watcher) {
            return false;
        }

        @Override
        public boolean removeWatcher(String path, Watcher watcher) {
            return false;
        }

        @Override
        public void removeWatcher(Watcher watcher) {
            // NOP
        }

        @Override
        public WatcherOrBitSet triggerWatch(String path, Watcher.Event.EventType type) {
            return new WatcherOrBitSet(Collections.emptySet());
        }

        @Override
        public WatcherOrBitSet triggerWatch(String path, Watcher.Event.EventType type, WatcherOrBitSet suppress) {
            return new WatcherOrBitSet(Collections.emptySet());
        }

        @Override
        public int size() {
            return 0;
        }

        @Override
        public void shutdown() {
            // NOP
        }

        @Override
        public WatchesSummary getWatchesSummary() {
            return null;
        }

        @Override
        public WatchesReport getWatches() {
            return null;
        }

        @Override
        public WatchesPathReport getWatchesByPath() {
            return null;
        }

        @Override
        public void dumpWatches(PrintWriter pwriter, boolean byPath) {
            // NOP
        }
    }

    @Before
    public void setUp() throws Exception {
        System.setProperty(WatchManagerFactory.ZOOKEEPER_WATCH_MANAGER_NAME, StubbedWatchManager.class.getName());
        super.setUp();
    }

    @After
    public void tearDown() throws Exception {
        try {
            super.tearDown();
        } finally {
            System.clearProperty(WatchManagerFactory.ZOOKEEPER_WATCH_MANAGER_NAME);
        }
    }

    @Test(expected = KeeperException.MarshallingErrorException.class)
    public void testBehavior() throws IOException, InterruptedException, KeeperException {
        try (ZooKeeper zk = createClient(hostPort)) {
            // the server will generate an exception as our custom watch manager doesn't implement
            // the new version of addWatch()
            zk.addWatch("/foo", event -> {
            }, AddWatchMode.PERSISTENT_RECURSIVE);
        }
    }
}
