package org.testfx.lifecycle.impl;

import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Future;
import java.util.function.Consumer;
import java.util.function.Supplier;
import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import org.testfx.lifecycle.LifecycleService;

import static org.loadui.testfx.utils.RunWaitUtils.callLater;
import static org.loadui.testfx.utils.RunWaitUtils.callOutside;
import static org.loadui.testfx.utils.RunWaitUtils.runLater;
import static org.loadui.testfx.utils.RunWaitUtils.runOutside;

public class LifecycleServiceImpl implements LifecycleService {

    // TODO: Platform.isFxApplicationThread() ? throw new RuntimeException("Dont run here");

    public Future<Stage> setupPrimaryStage(Future<Stage> primaryStageFuture,
                                           Class<? extends Application> toolkitApplication) {

        if (!primaryStageFuture.isDone()) {
            runOutside(() -> Application.launch(toolkitApplication));
        }
        return primaryStageFuture;
    }

    public Future<Void> setup(Runnable runnable) {
        return runLater(runnable);
    }

    public <T> Future<T> setup(Callable<T> callable) {
        return callLater(callable);
    }

    public Future<Stage> setupStage(Stage stage,
                                    Consumer<Stage> stageConsumer) {
        return callLater(() -> {
            stageConsumer.accept(stage);
            return stage;
        });
    }

    public Future<Scene> setupScene(Stage stage,
                                    Supplier<? extends Scene> sceneSupplier) {
        return callLater(() -> {
            Scene scene = sceneSupplier.get();
            stage.setScene(scene);
            return scene;
        });
    }

    public Future<Parent> setupSceneRoot(Stage stage,
                                         Supplier<? extends Parent> sceneRootSupplier) {
        return callLater(() -> {
            Parent rootNode = sceneRootSupplier.get();
            stage.setScene(new Scene(rootNode));
            return rootNode;
        });
    }

    public Future<Application> setupApplication(Stage stage,
                                                Class<? extends Application> appClass) {
        return callLater(() -> {
            Application application = appClass.newInstance();
            CountDownLatch latch = new CountDownLatch(1);
            callOutside(() -> {
                try {
                    application.init();
                }
                finally {
                    latch.countDown();
                }
                return null;
            });
            // TODO: check if ok to block javafx thread.
            latch.await();
            try {
                application.start(stage);
            }
            catch (Throwable throwable) {
                throw throwable;
            }
            return application;
        });
    }

    public Future<Void> cleanupApplication(Application application) {
        return callLater(() -> {
            application.stop();
            return null;
        });
    }

}
