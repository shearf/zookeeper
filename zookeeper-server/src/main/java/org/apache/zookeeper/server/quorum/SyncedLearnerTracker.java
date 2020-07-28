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

package org.apache.zookeeper.server.quorum;

import org.apache.zookeeper.server.quorum.flexible.QuorumVerifier;

import java.util.ArrayList;
import java.util.HashSet;

/**
 * 投票追踪器
 *
 * 关联集群的投票结果
 */
public class SyncedLearnerTracker {

    protected final ArrayList<QuorumVerifierAckSetPair> qvAckSetPairs = new ArrayList<>();

    public void addQuorumVerifier(QuorumVerifier qv) {
        qvAckSetPairs.add(new QuorumVerifierAckSetPair(qv, new HashSet<>(qv.getVotingMembers().size())));
    }

    public boolean addAck(Long sid) {
        boolean change = false;
        for (QuorumVerifierAckSetPair qvAckSet : qvAckSetPairs) {
            if (qvAckSet.getQuorumVerifier().getVotingMembers().containsKey(sid)) {
                qvAckSet.getAckSet().add(sid);
                change = true;
            }
        }
        return change;
    }

    public boolean hasSid(long sid) {
        for (QuorumVerifierAckSetPair qvAckSet : qvAckSetPairs) {
            if (!qvAckSet.getQuorumVerifier().getVotingMembers().containsKey(sid)) {
                return false;
            }
        }
        return true;
    }

    /**
     * 已应答的投票结果 = 整个集群
     * @return
     */
    public boolean hasAllQuorums() {
        for (QuorumVerifierAckSetPair qvAckSet : qvAckSetPairs) {
            if (!qvAckSet.getQuorumVerifier().containsQuorum(qvAckSet.getAckSet())) {
                return false;
            }
        }
        return true;
    }

    public String ackSetsToString() {
        StringBuilder sb = new StringBuilder();

        for (QuorumVerifierAckSetPair qvAckSet : qvAckSetPairs) {
            sb.append(qvAckSet.getAckSet().toString()).append(",");
        }

        return sb.substring(0, sb.length() - 1);
    }

    /**
     * 集群与响应的集合，成队关联
     */
    public static class QuorumVerifierAckSetPair {

        private final QuorumVerifier qv;
        private final HashSet<Long> ackSet;

        public QuorumVerifierAckSetPair(QuorumVerifier qv, HashSet<Long> ackSet) {
            this.qv = qv;
            this.ackSet = ackSet;
        }

        public QuorumVerifier getQuorumVerifier() {
            return this.qv;
        }

        public HashSet<Long> getAckSet() {
            return this.ackSet;
        }

    }

}
