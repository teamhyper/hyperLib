package org.hyperonline.hyperlib.vision;

import org.opencv.core.Rect;

import java.util.List;

/**
 * Base class with common functionality used by TargetProcessors. In particular,
 * this provides methods to store a result, and access it via PID sources.
 *
 * @param <T> VisionResult type of this AbstractTargetProcessor
 * @author James Hagborg
 */
public abstract class AbstractTargetProcessor<T extends VisionResult> implements TargetProcessor {

    private AbstractVisionConnector<T> m_visionConnector;

    protected AbstractTargetProcessor(AbstractVisionConnector<T> connector) {
        m_visionConnector = connector;
    }

    /**
     * @param targets list of targets to compute
     * @return {T}
     */
    public abstract T computeResult(List<Rect> targets);

    protected T getLastResult(){
        return m_visionConnector.getLastResult();
    }

    /**
     *
     * @param targets The list of targets found.
     */
    @Override
    public final void process(List<Rect> targets) {
        T res = computeResult(targets);
        m_visionConnector.updateResult(res);
    }
}

