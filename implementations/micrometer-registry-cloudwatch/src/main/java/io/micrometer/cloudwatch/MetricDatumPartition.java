/**
 * Copyright 2017 Pivotal Software, Inc.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * https://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.micrometer.cloudwatch;

import com.amazonaws.services.cloudwatch.model.MetricDatum;
import io.micrometer.core.instrument.util.MathUtils;

import java.util.AbstractList;
import java.util.List;

/**
 * Modified from {@link io.micrometer.core.instrument.util.MeterPartition}.
 *
 * @author Dawid Kublik
 * @deprecated the micrometer-registry-cloudwatch implementation has been deprecated in favour of
 *             micrometer-registry-cloudwatch2, which uses AWS SDK for Java 2.x
 */
@Deprecated
public class MetricDatumPartition extends AbstractList<List<MetricDatum>> {
    private final List<MetricDatum> list;
    private final int partitionSize;
    private final int partitionCount;

    public MetricDatumPartition(List<MetricDatum> metricData, int partitionSize) {
        this.list = metricData;
        this.partitionSize = partitionSize;
        this.partitionCount = MathUtils.divideWithCeilingRoundingMode(list.size(), partitionSize);
    }

    public static List<List<MetricDatum>> partition(List<MetricDatum> metricData, int partitionSize) {
        return new MetricDatumPartition(metricData, partitionSize);
    }

    @Override
    public List<MetricDatum> get(int index) {
        int start = index * partitionSize;
        int end = Math.min(start + partitionSize, list.size());
        return list.subList(start, end);
    }

    @Override
    public int size() {
        return this.partitionCount;
    }

    @Override
    public boolean isEmpty() {
        return list.isEmpty();
    }
}
