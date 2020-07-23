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

package org.apache.zookeeper;

import org.apache.yetus.audience.InterfaceAudience;

import java.util.*;

@SuppressWarnings("serial")
@InterfaceAudience.Public
public abstract class KeeperException extends Exception {

    /**
     * All multi-requests that result in an exception retain the results
     * here so that it is possible to examine the problems in the catch
     * scope.  Non-multi requests will get a null if they try to access
     * these results.
     */
    private List<OpResult> results;

    /**
     * All non-specific keeper exceptions should be constructed via
     * this factory method in order to guarantee consistency in error
     * codes and such.  If you know the error code, then you should
     * construct the special purpose exception directly.  That will
     * allow you to have the most specific possible declarations of
     * what exceptions might actually be thrown.
     *
     * @param code The error code.
     * @param path The ZooKeeper path being operated on.
     * @return The specialized exception, presumably to be thrown by
     * the caller.
     */
    public static KeeperException create(Code code, String path) {
        KeeperException r = create(code);
        r.path = path;
        return r;
    }

    /**
     * @deprecated deprecated in 3.1.0, use {@link #create(Code, String)}
     * instead
     */
    @Deprecated
    public static KeeperException create(int code, String path) {
        KeeperException r = create(Code.get(code));
        r.path = path;
        return r;
    }

    /**
     * @deprecated deprecated in 3.1.0, use {@link #create(Code)}
     * instead
     */
    @Deprecated
    public static KeeperException create(int code) {
        return create(Code.get(code));
    }

    /**
     * All non-specific keeper exceptions should be constructed via
     * this factory method in order to guarantee consistency in error
     * codes and such.  If you know the error code, then you should
     * construct the special purpose exception directly.  That will
     * allow you to have the most specific possible declarations of
     * what exceptions might actually be thrown.
     *
     * @param code The error code of your new exception.  This will
     *             also determine the specific type of the exception that is
     *             returned.
     * @return The specialized exception, presumably to be thrown by
     * the caller.
     */
    public static KeeperException create(Code code) {
        switch (code) {
            case SYSTEM_ERROR:
                return new SystemErrorException();
            case RUNTIME_INCONSISTENCY:
                return new RuntimeInconsistencyException();
            case DATA_INCONSISTENCY:
                return new DataInconsistencyException();
            case CONNECTION_LOSS:
                return new ConnectionLossException();
            case MARSHALLING_ERROR:
                return new MarshallingErrorException();
            case UNIMPLEMENTED:
                return new UnimplementedException();
            case OPERATION_TIMEOUT:
                return new OperationTimeoutException();
            case NEW_CONFIG_NO_QUORUM:
                return new NewConfigNoQuorum();
            case RECONFIG_IN_PROGRESS:
                return new ReconfigInProgress();
            case BAD_ARGUMENTS:
                return new BadArgumentsException();
            case API_ERROR:
                return new APIErrorException();
            case NO_NODE:
                return new NoNodeException();
            case NO_AUTH:
                return new NoAuthException();
            case BAD_VERSION:
                return new BadVersionException();
            case NO_CHILDREN_FOR_EPHEMERALS:
                return new NoChildrenForEphemeralsException();
            case NODE_EXISTS:
                return new NodeExistsException();
            case INVALID_ACL:
                return new InvalidACLException();
            case AUTH_FAILED:
                return new AuthFailedException();
            case NOT_EMPTY:
                return new NotEmptyException();
            case SESSION_EXPIRED:
                return new SessionExpiredException();
            case INVALID_CALLBACK:
                return new InvalidCallbackException();
            case SESSION_MOVED:
                return new SessionMovedException();
            case NOT_READONLY:
                return new NotReadOnlyException();
            case EPHEMERAL_ON_LOCAL_SESSION:
                return new EphemeralOnLocalSessionException();
            case NO_WATCHER:
                return new NoWatcherException();
            case RECONFIG_DISABLED:
                return new ReconfigDisabledException();
            case SESSION_CLOSED_REQUIRE_SASL_AUTH:
                return new SessionClosedRequireAuthException();
            case REQUEST_TIMEOUT:
                return new RequestTimeoutException();
            case THROTTLED_OP:
                return new ThrottledOpException();
            case OK:
            default:
                throw new IllegalArgumentException("Invalid exception code");
        }
    }

    /**
     * Set the code for this exception
     *
     * @param code error code
     * @deprecated deprecated in 3.1.0, exceptions should be immutable, this
     * method should not be used
     */
    @Deprecated
    public void setCode(int code) {
        this.code = Code.get(code);
    }

    /**
     * This interface contains the original static final int constants
     * which have now been replaced with an enumeration in Code. Do not
     * reference this class directly, if necessary (legacy code) continue
     * to access the constants through Code.
     * Note: an interface is used here due to the fact that enums cannot
     * reference constants defined within the same enum as said constants
     * are considered initialized _after_ the enum itself. By using an
     * interface as a super type this allows the deprecated constants to
     * be initialized first and referenced when constructing the enums. I
     * didn't want to have constants declared twice. This
     * interface should be private, but it's declared public to enable
     * javadoc to include in the user API spec.
     */
    @Deprecated
    @InterfaceAudience.Public
    public interface CodeDeprecated {

        /**
         * @deprecated deprecated in 3.1.0, use {@link Code#OK} instead
         */
        @Deprecated
        int Ok = 0;

        /**
         * @deprecated deprecated in 3.1.0, use {@link Code#SYSTEM_ERROR} instead
         */
        @Deprecated
        int SystemError = -1;
        /**
         * @deprecated deprecated in 3.1.0, use
         * {@link Code#RUNTIME_INCONSISTENCY} instead
         */
        @Deprecated
        int RuntimeInconsistency = -2;
        /**
         * @deprecated deprecated in 3.1.0, use {@link Code#DATA_INCONSISTENCY}
         * instead
         */
        @Deprecated
        int DataInconsistency = -3;
        /**
         * @deprecated deprecated in 3.1.0, use {@link Code#CONNECTION_LOSS}
         * instead
         */
        @Deprecated
        int ConnectionLoss = -4;
        /**
         * @deprecated deprecated in 3.1.0, use {@link Code#MARSHALLING_ERROR}
         * instead
         */
        @Deprecated
        int MarshallingError = -5;
        /**
         * @deprecated deprecated in 3.1.0, use {@link Code#UNIMPLEMENTED}
         * instead
         */
        @Deprecated
        int Unimplemented = -6;
        /**
         * @deprecated deprecated in 3.1.0, use {@link Code#OPERATION_TIMEOUT}
         * instead
         */
        @Deprecated
        int OperationTimeout = -7;
        /**
         * @deprecated deprecated in 3.1.0, use {@link Code#BAD_ARGUMENTS}
         * instead
         */
        @Deprecated
        int BadArguments = -8;

        @Deprecated
        int UnknownSession = -12;

        /**
         * @deprecated deprecated in 3.1.0, use {@link Code#NEW_CONFIG_NO_QUORUM}
         * instead
         */
        @Deprecated
        int NewConfigNoQuorum = -13;

        /**
         * @deprecated deprecated in 3.1.0, use {@link Code#RECONFIG_IN_PROGRESS}
         * instead
         */
        @Deprecated
        int ReconfigInProgress = -14;

        /**
         * @deprecated deprecated in 3.1.0, use {@link Code#API_ERROR} instead
         */
        @Deprecated
        int APIError = -100;

        /**
         * @deprecated deprecated in 3.1.0, use {@link Code#NO_NODE} instead
         */
        @Deprecated
        int NoNode = -101;
        /**
         * @deprecated deprecated in 3.1.0, use {@link Code#NO_AUTH} instead
         */
        @Deprecated
        int NoAuth = -102;
        /**
         * @deprecated deprecated in 3.1.0, use {@link Code#BAD_VERSION} instead
         */
        @Deprecated
        int BadVersion = -103;
        /**
         * @deprecated deprecated in 3.1.0, use
         * {@link Code#NO_CHILDREN_FOR_EPHEMERALS}
         * instead
         */
        @Deprecated
        int NoChildrenForEphemerals = -108;
        /**
         * @deprecated deprecated in 3.1.0, use {@link Code#NODE_EXISTS} instead
         */
        @Deprecated
        int NodeExists = -110;
        /**
         * @deprecated deprecated in 3.1.0, use {@link Code#NOT_EMPTY} instead
         */
        @Deprecated
        int NotEmpty = -111;
        /**
         * @deprecated deprecated in 3.1.0, use {@link Code#SESSION_EXPIRED} instead
         */
        @Deprecated
        int SessionExpired = -112;
        /**
         * @deprecated deprecated in 3.1.0, use {@link Code#INVALID_CALLBACK}
         * instead
         */
        @Deprecated
        int InvalidCallback = -113;
        /**
         * @deprecated deprecated in 3.1.0, use {@link Code#INVALID_ACL} instead
         */
        @Deprecated
        int InvalidACL = -114;
        /**
         * @deprecated deprecated in 3.1.0, use {@link Code#AUTH_FAILED} instead
         */
        @Deprecated
        int AuthFailed = -115;

        // This value will be used directly in {@link CODE#SESSIONMOVED}
        // public static final int SessionMoved = -118;

        @Deprecated
        int EphemeralOnLocalSession = -120;

    }

    /**
     * Codes which represent the various KeeperException
     * types. This enum replaces the deprecated earlier static final int
     * constants. The old, deprecated, values are in "camel case" while the new
     * enum values are in all CAPS.
     */
    @InterfaceAudience.Public
    public enum Code implements CodeDeprecated {
        /**
         * Everything is OK
         */
        OK(Ok),

        /**
         * System and server-side errors.
         * This is never thrown by the server, it shouldn't be used other than
         * to indicate a range. Specifically error codes greater than this
         * value, but lesser than {@link #API_ERROR}, are system errors.
         */
        SYSTEM_ERROR(SystemError),

        /**
         * A runtime inconsistency was found
         */
        RUNTIME_INCONSISTENCY(RuntimeInconsistency),
        /**
         * A data inconsistency was found
         */
        DATA_INCONSISTENCY(DataInconsistency),
        /**
         * Connection to the server has been lost
         */
        CONNECTION_LOSS(ConnectionLoss),
        /**
         * Error while marshalling or unmarshalling data
         */
        MARSHALLING_ERROR(MarshallingError),
        /**
         * Operation is unimplemented
         */
        UNIMPLEMENTED(Unimplemented),
        /**
         * Operation timeout
         */
        OPERATION_TIMEOUT(OperationTimeout),
        /**
         * Invalid arguments
         */
        BAD_ARGUMENTS(BadArguments),
        /**
         * No quorum of new config is connected and up-to-date with the leader of last commmitted config - try
         * invoking reconfiguration after new servers are connected and synced
         */
        NEW_CONFIG_NO_QUORUM(NewConfigNoQuorum),
        /**
         * Another reconfiguration is in progress -- concurrent reconfigs not supported (yet)
         */
        RECONFIG_IN_PROGRESS(ReconfigInProgress),
        /**
         * Unknown session (internal server use only)
         */
        UNKNOWN_SESSION(UnknownSession),

        /**
         * API errors.
         * This is never thrown by the server, it shouldn't be used other than
         * to indicate a range. Specifically error codes greater than this
         * value are API errors (while values less than this indicate a
         * {@link #SYSTEM_ERROR}).
         */
        API_ERROR(APIError),

        /**
         * Node does not exist
         */
        NO_NODE(NoNode),
        /**
         * Not authenticated
         */
        NO_AUTH(NoAuth),
        /**
         * Version conflict
         * In case of reconfiguration: reconfig requested from config version X but last seen config has a different version Y
         */
        BAD_VERSION(BadVersion),
        /**
         * Ephemeral nodes may not have children
         */
        NO_CHILDREN_FOR_EPHEMERALS(NoChildrenForEphemerals),
        /**
         * The node already exists
         */
        NODE_EXISTS(NodeExists),
        /**
         * The node has children
         */
        NOT_EMPTY(NotEmpty),
        /**
         * The session has been expired by the server
         */
        SESSION_EXPIRED(SessionExpired),
        /**
         * Invalid callback specified
         */
        INVALID_CALLBACK(InvalidCallback),
        /**
         * Invalid ACL specified
         */
        INVALID_ACL(InvalidACL),
        /**
         * Client authentication failed
         */
        AUTH_FAILED(AuthFailed),
        /**
         * Session moved to another server, so operation is ignored
         */
        SESSION_MOVED(-118),
        /**
         * State-changing request is passed to read-only server
         */
        NOT_READONLY(-119),
        /**
         * Attempt to create ephemeral node on a local session
         */
        EPHEMERAL_ON_LOCAL_SESSION(EphemeralOnLocalSession),
        /**
         * Attempts to remove a non-existing watcher
         */
        NO_WATCHER(-121),
        /**
         * Request not completed within max allowed time.
         */
        REQUEST_TIMEOUT(-122),
        /**
         * Attempts to perform a reconfiguration operation when reconfiguration feature is disabled.
         */
        RECONFIG_DISABLED(-123),
        /**
         * The session has been closed by server because server requires client to do SASL authentication,
         * but client is not configured with SASL authentication or configuted with SASL but failed
         * (i.e. wrong credential used.).
         */
        SESSION_CLOSED_REQUIRE_SASL_AUTH(-124),
        /**
         * Operation was throttled and not executed at all. This error code indicates that zookeeper server
         * is under heavy load and can't process incoming requests at full speed; please retry with back off.
         */
        THROTTLED_OP(-127);

        private static final Map<Integer, Code> lookup = new HashMap<Integer, Code>();

        static {
            for (Code c : EnumSet.allOf(Code.class)) {
                lookup.put(c.code, c);
            }
        }

        private final int code;

        Code(int code) {
            this.code = code;
        }

        /**
         * Get the int value for a particular Code.
         *
         * @return error code as integer
         */
        public int intValue() {
            return code;
        }

        /**
         * Get the Code value for a particular integer error code
         *
         * @param code int error code
         * @return Code value corresponding to specified int code, or null
         */
        public static Code get(int code) {
            return lookup.get(code);
        }
    }

    static String getCodeMessage(Code code) {
        switch (code) {
            case OK:
                return "ok";
            case SYSTEM_ERROR:
                return "SystemError";
            case RUNTIME_INCONSISTENCY:
                return "RuntimeInconsistency";
            case DATA_INCONSISTENCY:
                return "DataInconsistency";
            case CONNECTION_LOSS:
                return "ConnectionLoss";
            case MARSHALLING_ERROR:
                return "MarshallingError";
            case NEW_CONFIG_NO_QUORUM:
                return "NewConfigNoQuorum";
            case RECONFIG_IN_PROGRESS:
                return "ReconfigInProgress";
            case UNIMPLEMENTED:
                return "Unimplemented";
            case OPERATION_TIMEOUT:
                return "OperationTimeout";
            case BAD_ARGUMENTS:
                return "BadArguments";
            case API_ERROR:
                return "APIError";
            case NO_NODE:
                return "NoNode";
            case NO_AUTH:
                return "NoAuth";
            case BAD_VERSION:
                return "BadVersion";
            case NO_CHILDREN_FOR_EPHEMERALS:
                return "NoChildrenForEphemerals";
            case NODE_EXISTS:
                return "NodeExists";
            case INVALID_ACL:
                return "InvalidACL";
            case AUTH_FAILED:
                return "AuthFailed";
            case NOT_EMPTY:
                return "Directory not empty";
            case SESSION_EXPIRED:
                return "Session expired";
            case INVALID_CALLBACK:
                return "Invalid callback";
            case SESSION_MOVED:
                return "Session moved";
            case NOT_READONLY:
                return "Not a read-only call";
            case EPHEMERAL_ON_LOCAL_SESSION:
                return "Ephemeral node on local session";
            case NO_WATCHER:
                return "No such watcher";
            case RECONFIG_DISABLED:
                return "Reconfig is disabled";
            case SESSION_CLOSED_REQUIRE_SASL_AUTH:
                return "Session closed because client failed to authenticate";
            case THROTTLED_OP:
                return "Op throttled due to high load";
            default:
                return "Unknown error " + code;
        }
    }

    private Code code;

    private String path;

    public KeeperException(Code code) {
        this.code = code;
    }

    KeeperException(Code code, String path) {
        this.code = code;
        this.path = path;
    }

    /**
     * Read the error code for this exception
     *
     * @return the error code for this exception
     * @deprecated deprecated in 3.1.0, use {@link #code()} instead
     */
    @Deprecated
    public int getCode() {
        return code.code;
    }

    /**
     * Read the error Code for this exception
     *
     * @return the error Code for this exception
     */
    public Code code() {
        return code;
    }

    /**
     * Read the path for this exception
     *
     * @return the path associated with this error, null if none
     */
    public String getPath() {
        return path;
    }

    @Override
    public String getMessage() {
        if (path == null || path.isEmpty()) {
            return "KeeperErrorCode = " + getCodeMessage(code);
        }
        return "KeeperErrorCode = " + getCodeMessage(code) + " for " + path;
    }

    void setMultiResults(List<OpResult> results) {
        this.results = results;
    }

    /**
     * If this exception was thrown by a multi-request then the (partial) results
     * and error codes can be retrieved using this getter.
     *
     * @return A copy of the list of results from the operations in the multi-request.
     * @since 3.4.0
     */
    public List<OpResult> getResults() {
        return results != null ? new ArrayList<OpResult>(results) : null;
    }

    /**
     * @see Code#API_ERROR
     */
    @InterfaceAudience.Public
    public static class APIErrorException extends KeeperException {

        public APIErrorException() {
            super(Code.API_ERROR);
        }

    }

    /**
     * @see Code#AUTH_FAILED
     */
    @InterfaceAudience.Public
    public static class AuthFailedException extends KeeperException {

        public AuthFailedException() {
            super(Code.AUTH_FAILED);
        }

    }

    /**
     * @see Code#BAD_ARGUMENTS
     */
    @InterfaceAudience.Public
    public static class BadArgumentsException extends KeeperException {

        public BadArgumentsException() {
            super(Code.BAD_ARGUMENTS);
        }

        public BadArgumentsException(String path) {
            super(Code.BAD_ARGUMENTS, path);
        }

    }

    /**
     * @see Code#BAD_VERSION
     */
    @InterfaceAudience.Public
    public static class BadVersionException extends KeeperException {

        public BadVersionException() {
            super(Code.BAD_VERSION);
        }

        public BadVersionException(String path) {
            super(Code.BAD_VERSION, path);
        }

    }

    /**
     * @see Code#CONNECTION_LOSS
     */
    @InterfaceAudience.Public
    public static class ConnectionLossException extends KeeperException {

        public ConnectionLossException() {
            super(Code.CONNECTION_LOSS);
        }

    }

    /**
     * @see Code#DATA_INCONSISTENCY
     */
    @InterfaceAudience.Public
    public static class DataInconsistencyException extends KeeperException {

        public DataInconsistencyException() {
            super(Code.DATA_INCONSISTENCY);
        }

    }

    /**
     * @see Code#INVALID_ACL
     */
    @InterfaceAudience.Public
    public static class InvalidACLException extends KeeperException {

        public InvalidACLException() {
            super(Code.INVALID_ACL);
        }

        public InvalidACLException(String path) {
            super(Code.INVALID_ACL, path);
        }

    }

    /**
     * @see Code#INVALID_CALLBACK
     */
    @InterfaceAudience.Public
    public static class InvalidCallbackException extends KeeperException {

        public InvalidCallbackException() {
            super(Code.INVALID_CALLBACK);
        }

    }

    /**
     * @see Code#MARSHALLING_ERROR
     */
    @InterfaceAudience.Public
    public static class MarshallingErrorException extends KeeperException {

        public MarshallingErrorException() {
            super(Code.MARSHALLING_ERROR);
        }

    }

    /**
     * @see Code#NO_AUTH
     */
    @InterfaceAudience.Public
    public static class NoAuthException extends KeeperException {

        public NoAuthException() {
            super(Code.NO_AUTH);
        }

    }

    /**
     * @see Code#NEW_CONFIG_NO_QUORUM
     */
    @InterfaceAudience.Public
    public static class NewConfigNoQuorum extends KeeperException {

        public NewConfigNoQuorum() {
            super(Code.NEW_CONFIG_NO_QUORUM);
        }

    }

    /**
     * @see Code#RECONFIG_IN_PROGRESS
     */
    @InterfaceAudience.Public
    public static class ReconfigInProgress extends KeeperException {

        public ReconfigInProgress() {
            super(Code.RECONFIG_IN_PROGRESS);
        }

    }

    /**
     * @see Code#NO_CHILDREN_FOR_EPHEMERALS
     */
    @InterfaceAudience.Public
    public static class NoChildrenForEphemeralsException extends KeeperException {

        public NoChildrenForEphemeralsException() {
            super(Code.NO_CHILDREN_FOR_EPHEMERALS);
        }

        public NoChildrenForEphemeralsException(String path) {
            super(Code.NO_CHILDREN_FOR_EPHEMERALS, path);
        }

    }

    /**
     * @see Code#NODE_EXISTS
     */
    @InterfaceAudience.Public
    public static class NodeExistsException extends KeeperException {

        public NodeExistsException() {
            super(Code.NODE_EXISTS);
        }

        public NodeExistsException(String path) {
            super(Code.NODE_EXISTS, path);
        }

    }

    /**
     * @see Code#NO_NODE
     */
    @InterfaceAudience.Public
    public static class NoNodeException extends KeeperException {

        public NoNodeException() {
            super(Code.NO_NODE);
        }

        public NoNodeException(String path) {
            super(Code.NO_NODE, path);
        }

    }

    /**
     * @see Code#NOT_EMPTY
     */
    @InterfaceAudience.Public
    public static class NotEmptyException extends KeeperException {

        public NotEmptyException() {
            super(Code.NOT_EMPTY);
        }

        public NotEmptyException(String path) {
            super(Code.NOT_EMPTY, path);
        }

    }

    /**
     * @see Code#OPERATION_TIMEOUT
     */
    @InterfaceAudience.Public
    public static class OperationTimeoutException extends KeeperException {

        public OperationTimeoutException() {
            super(Code.OPERATION_TIMEOUT);
        }

    }

    /**
     * @see Code#RUNTIME_INCONSISTENCY
     */
    @InterfaceAudience.Public
    public static class RuntimeInconsistencyException extends KeeperException {

        public RuntimeInconsistencyException() {
            super(Code.RUNTIME_INCONSISTENCY);
        }

    }

    /**
     * @see Code#SESSION_EXPIRED
     */
    @InterfaceAudience.Public
    public static class SessionExpiredException extends KeeperException {

        public SessionExpiredException() {
            super(Code.SESSION_EXPIRED);
        }

    }

    /**
     * @see Code#UNKNOWN_SESSION
     */
    @InterfaceAudience.Public
    public static class UnknownSessionException extends KeeperException {

        public UnknownSessionException() {
            super(Code.UNKNOWN_SESSION);
        }

    }

    /**
     * @see Code#SESSION_MOVED
     */
    @InterfaceAudience.Public
    public static class SessionMovedException extends KeeperException {

        public SessionMovedException() {
            super(Code.SESSION_MOVED);
        }

    }

    /**
     * @see Code#NOT_READONLY
     */
    @InterfaceAudience.Public
    public static class NotReadOnlyException extends KeeperException {

        public NotReadOnlyException() {
            super(Code.NOT_READONLY);
        }

    }

    /**
     * @see Code#EPHEMERAL_ON_LOCAL_SESSION
     */
    @InterfaceAudience.Public
    public static class EphemeralOnLocalSessionException extends KeeperException {

        public EphemeralOnLocalSessionException() {
            super(Code.EPHEMERAL_ON_LOCAL_SESSION);
        }

    }

    /**
     * @see Code#SYSTEM_ERROR
     */
    @InterfaceAudience.Public
    public static class SystemErrorException extends KeeperException {

        public SystemErrorException() {
            super(Code.SYSTEM_ERROR);
        }

    }

    /**
     * @see Code#UNIMPLEMENTED
     */
    @InterfaceAudience.Public
    public static class UnimplementedException extends KeeperException {

        public UnimplementedException() {
            super(Code.UNIMPLEMENTED);
        }

    }

    /**
     * @see Code#NO_WATCHER
     */
    @InterfaceAudience.Public
    public static class NoWatcherException extends KeeperException {

        public NoWatcherException() {
            super(Code.NO_WATCHER);
        }

        public NoWatcherException(String path) {
            super(Code.NO_WATCHER, path);
        }

    }

    /**
     * @see Code#RECONFIG_DISABLED
     */
    @InterfaceAudience.Public
    public static class ReconfigDisabledException extends KeeperException {

        public ReconfigDisabledException() {
            super(Code.RECONFIG_DISABLED);
        }

        public ReconfigDisabledException(String path) {
            super(Code.RECONFIG_DISABLED, path);
        }

    }

    /**
     * @see Code#SESSION_CLOSED_REQUIRE_SASL_AUTH
     */
    public static class SessionClosedRequireAuthException extends KeeperException {

        public SessionClosedRequireAuthException() {
            super(Code.SESSION_CLOSED_REQUIRE_SASL_AUTH);
        }

        public SessionClosedRequireAuthException(String path) {
            super(Code.SESSION_CLOSED_REQUIRE_SASL_AUTH, path);
        }

    }

    /**
     * @see Code#REQUEST_TIMEOUT
     */
    public static class RequestTimeoutException extends KeeperException {

        public RequestTimeoutException() {
            super(Code.REQUEST_TIMEOUT);
        }

    }

    /**
     * @see Code#THROTTLED_OP
     */
    public static class ThrottledOpException extends KeeperException {
        public ThrottledOpException() {
            super(Code.THROTTLED_OP);
        }
    }
}
