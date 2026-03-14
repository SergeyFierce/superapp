package ru.topskiy.superapp.services.planner

import ru.topskiy.superapp.core.bootstrap.AppBootstrap
import ru.topskiy.superapp.services.planner.impl.PlannerCommandHandler
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PlannerBootstrap @Inject constructor(
    private val handler: PlannerCommandHandler,
) : AppBootstrap
