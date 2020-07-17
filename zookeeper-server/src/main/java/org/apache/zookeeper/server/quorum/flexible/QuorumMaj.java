/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.zookeeper.server.quorum.flexible;

import org.apache.zookeeper.server.quorum.QuorumPeer.LearnerType;
import org.apache.zookeeper.server.quorum.QuorumPeer.QuorumServer;
import org.apache.zookeeper.server.quorum.QuorumPeerConfig.ConfigException;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;

/**
 * This class implements a validator for majority quorums. The implementation is
 * straightforward.
 */
public class QuorumMaj implements QuorumVerifier {

    private Map<Long, QuorumServer> allMembers = new HashMap<Long, QuorumServer>();
    private Map<Long, QuorumServer> votingMembers = new HashMap<Long, QuorumServer>();
    private Map<Long, QuorumServer> observingMembers = new HashMap<Long, QuorumServer>();
    private long version = 0;
    private int half;

    public int hashCode() {
        assert false : "hashCode not designed";
        return 42; // any arbitrary constant will do
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof QuorumMaj)) {
            return false;
        }
        QuorumMaj qm = (QuorumMaj) o;
        if (qm.getVersion() == version) {
            return true;
        }
        if (allMembers.size() != qm.getAllMembers().size()) {
            return false;
        }
        for (QuorumServer qs : allMembers.values()) {
            QuorumServer qso = qm.getAllMembers().get(qs.id);
            if (qso == null || !qs.equals(qso)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Defines a majority to avoid computing it every time.
     */
    public QuorumMaj(Map<Long, QuorumServer> allMembers) {
        this.allMembers = allMembers;
        for (QuorumServer qs : allMembers.values()) {
            if (qs.type == LearnerType.PARTICIPANT) {
                votingMembers.put(qs.id, qs);
            } else {
                observingMembers.put(qs.id, qs);
            }
        }
        half = votingMembers.size() / 2;
    }

    public QuorumMaj(Properties props) throws ConfigException {
        for (Entry<Object, Object> entry : props.entrySet()) {
            String key = entry.getKey().toString();
            String value = entry.getValue().toString();

            if (key.startsWith("server.")) {
                int dot = key.indexOf('.');
                long sid = Long.parseLong(key.substring(dot + 1));
                QuorumServer qs = new QuorumServer(sid, value);
                allMembers.put(sid, qs);
                if (qs.type == LearnerType.PARTICIPANT) {
                    votingMembers.put(sid, qs);
                } else {
                    observingMembers.put(sid, qs);
                }
            } else if ("version".equals(key)) {
                version = Long.parseLong(value, 16);
            }
        }
        half = votingMembers.size() / 2;
    }

    /**
     * Returns weight of 1 by default.
     *
     * @param id
     */
    @Override
    public long getWeight(long id) {
        return 1;
    }

    @Override
    public String toString() {
        StringBuilder sw = new StringBuilder();

        for (QuorumServer member : getAllMembers().values()) {
            String key = "server." + member.id;
            String value = member.toString();
            sw.append(key);
            sw.append('=');
            sw.append(value);
            sw.append('\n');
        }
        String hexVersion = Long.toHexString(version);
        sw.append("version=");
        sw.append(hexVersion);
        return sw.toString();
    }

    /**
     * Verifies if a set is a majority. Assumes that ackSet contains acks only
     * from votingMembers
     */
    @Override
    public boolean containsQuorum(Set<Long> ackSet) {
        return (ackSet.size() > half);
    }

    @Override
    public Map<Long, QuorumServer> getAllMembers() {
        return allMembers;
    }

    @Override
    public Map<Long, QuorumServer> getVotingMembers() {
        return votingMembers;
    }

    @Override
    public Map<Long, QuorumServer> getObservingMembers() {
        return observingMembers;
    }

    @Override
    public long getVersion() {
        return version;
    }

    @Override
    public void setVersion(long ver) {
        version = ver;
    }

}
