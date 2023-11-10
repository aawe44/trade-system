package com.jason.trade.lightning.deal.service;

import com.jason.trade.lightning.deal.db.model.SeckillActivity;

import java.util.List;

public interface SeckillActivityService {
    /**
     * Inserts a new seckill activity.
     *
     * @param seckillActivity The seckill activity to insert.
     * @return true if the insertion was successful, false otherwise.
     */
    boolean insertSeckillActivity(SeckillActivity seckillActivity);

    /**
     * Retrieves a seckill activity by its unique ID.
     *
     * @param id The ID of the seckill activity to retrieve.
     * @return The seckill activity if found, or null if not found.
     */
    SeckillActivity querySeckillActivityById(long id);

    /**
     * Retrieves a list of seckill activities with the specified status.
     *
     * @param status The status of seckill activities to retrieve.
     * @return A list of seckill activities matching the given status.
     */
    List<SeckillActivity> queryActivitysByStatus(int status);

    /**
     * Processes a seckill request.
     *
     * @param seckillActivityId The ID of the seckill activity.
     * @return True if the seckill request was successfully processed, false otherwise.
     */
    boolean processSeckillReqBase(long seckillActivityId);

    /**
     * Processes a seckill request.
     *
     * @param seckillActivityId The ID of the seckill activity.
     * @return True if the seckill request was successfully processed, false otherwise.
     */
    boolean processSeckill(long seckillActivityId);


}