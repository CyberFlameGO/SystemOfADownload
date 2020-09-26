package org.spongepowered.downloads.test.buisness.maven;

import com.google.gson.Gson;
import org.junit.Test;
import org.spongepowered.downloads.buisness.maven.MavenImpl;
import org.spongepowered.downloads.config.AppConfig;
import org.spongepowered.downloads.pojo.data.MavenResource;

import java.util.Set;

public class TestAllPackages {

    @Test
    public void testWeGetSomethingBack() {
        final var defaultConfig = new AppConfig();
        final var gson = new Gson();

        final var sut = new MavenImpl(gson, defaultConfig);

        final Set<MavenResource> resources = sut.getAllResources(new AppConfig.Product("configurate-core")).join();

        if (resources.isEmpty()) {
            throw new IllegalStateException("resources should not be empty.");
        }
    }

}
