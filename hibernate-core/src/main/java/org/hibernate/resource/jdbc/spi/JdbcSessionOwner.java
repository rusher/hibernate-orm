/*
 * SPDX-License-Identifier: LGPL-2.1-or-later
 * Copyright Red Hat Inc. and Hibernate Authors
 */
package org.hibernate.resource.jdbc.spi;

import org.hibernate.engine.jdbc.connections.spi.JdbcConnectionAccess;
import org.hibernate.engine.jdbc.spi.SqlExceptionHelper;
import org.hibernate.event.spi.EventManager;
import org.hibernate.resource.transaction.spi.TransactionCoordinator;

/**
 * Contract for something that controls a {@link JdbcSessionContext}.
 * <p>
 * The term <em>JDBC session</em> is taken from the SQL specification which
 * calls a connection and its associated transaction context a "session".
 *
 * @apiNote The name comes from the design idea of a {@code JdbcSession}
 *           which encapsulates this information, which we will hopefully
 *           get back to later.
 *
 * @author Steve Ebersole
 */
public interface JdbcSessionOwner {

	JdbcSessionContext getJdbcSessionContext();

	JdbcConnectionAccess getJdbcConnectionAccess();

	/**
	 * Obtain the {@link TransactionCoordinator}.
	 *
	 * @return The {@code TransactionCoordinator}
	 */
	TransactionCoordinator getTransactionCoordinator();

	/**
	 * Callback indicating recognition of entering into a transactional
	 * context whether that is explicitly via the Hibernate
	 * {@link org.hibernate.Transaction} API or via registration
	 * of Hibernate's JTA Synchronization impl with a JTA Transaction
	 */
	void startTransactionBoundary();

	/**
	 * An after-begin callback from the coordinator to its owner.
	 */
	void afterTransactionBegin();

	/**
	 * A before-completion callback to the owner.
	 */
	void beforeTransactionCompletion();

	/**
	 * An after-completion callback to the owner.
	 *
	 * @param successful Was the transaction successful?
	 * @param delayed Is this a delayed after transaction completion call (aka after a timeout)?
	 */
	void afterTransactionCompletion(boolean successful, boolean delayed);

	void flushBeforeTransactionCompletion();

	/**
	 * Get the session-level JDBC batch size.
	 * @return session-level JDBC batch size
	 *
	 * @since 5.2
	 */
	Integer getJdbcBatchSize();

	EventManager getEventManager();

	default SqlExceptionHelper getSqlExceptionHelper() {
		return getJdbcSessionContext().getJdbcServices().getSqlExceptionHelper();
	}
}
