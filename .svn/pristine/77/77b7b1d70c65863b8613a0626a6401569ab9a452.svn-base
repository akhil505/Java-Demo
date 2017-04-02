package com.datamigration.dao;

import java.util.List;

import com.datamigration.util.FRDMException;

/**
 * @author DESS
 *
 * @param <T>
 */
public interface DaoUtil<T> {
	/**
	 * @param obj
	 * @return
	 * @throws FRDMException
	 */
	public T addUpdate(T obj) throws FRDMException;

	/**
	 * @param id
	 * @return
	 * @throws FRDMException
	 */
	public int delete(Object id) throws FRDMException;

	/**
	 * @return
	 * @throws FRDMException
	 */
	public List<T> getList() throws FRDMException;

	/**
	 * @param id
	 * @return
	 * @throws FRDMException
	 */
	public T getDetails(Object id) throws FRDMException;
}
