package com.fishing.conditions.di

import com.fishing.conditions.data.habitat.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object HabitatModule {

    @Provides
    @Singleton
    fun provideBathymetryRepository(): BathymetryRepository {
        return BathymetryRepository()
    }

    @Provides
    @Singleton
    fun provideStructureAnalyzer(): StructureAnalyzer {
        return StructureAnalyzer()
    }

    @Provides
    @Singleton
    fun provideCurrentInteractionModel(): CurrentInteractionModel {
        return CurrentInteractionModel()
    }

    @Provides
    @Singleton
    fun provideTemperatureGradientAnalyzer(): TemperatureGradientAnalyzer {
        return TemperatureGradientAnalyzer()
    }

    @Provides
    @Singleton
    fun provideHabitatSuitabilityEngine(
        bathymetryRepository: BathymetryRepository,
        structureAnalyzer: StructureAnalyzer,
        currentInteractionModel: CurrentInteractionModel,
        temperatureGradientAnalyzer: TemperatureGradientAnalyzer
    ): HabitatSuitabilityEngine {
        return HabitatSuitabilityEngine(
            bathymetryRepository,
            structureAnalyzer,
            currentInteractionModel,
            temperatureGradientAnalyzer
        )
    }

    @Provides
    @Singleton
    fun provideFishingSuitabilityCalculator(
        habitatEngine: HabitatSuitabilityEngine
    ): com.fishing.conditions.domain.FishingSuitabilityCalculator {
        return com.fishing.conditions.domain.FishingSuitabilityCalculator(habitatEngine)
    }
}
