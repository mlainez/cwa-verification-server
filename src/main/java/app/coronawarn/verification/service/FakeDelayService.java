/*
 * Corona-Warn-App / cwa-verification
 *
 * (C) 2020, T-Systems International GmbH
 * All modifications are copyright (c) 2020 Devside SRL.
 *
 * Deutsche Telekom AG and all other contributors /
 * copyright owners license this file to you under the Apache
 * License, Version 2.0 (the "License"); you may not use this
 * file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package app.coronawarn.verification.service;

import app.coronawarn.verification.config.VerificationApplicationConfig;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.math3.distribution.PoissonDistribution;
import org.springframework.stereotype.Component;

/**
 * {@link  FakeDelayService} instances manage the response delay in the processing of fake (or "dummy") requests.
 */
@Slf4j
@Component
public class FakeDelayService {

  private final long movingAverageSampleSize;

  private long fakeDelayTest;
  private long fakeDelayAck;
  private long fakeDelayTan;
  private long fakeDelayToken;

  /**
   * Constructor for the FakeDelayService.
   */
  public FakeDelayService(VerificationApplicationConfig applicationConfig) {
    this.fakeDelayTest = applicationConfig.getInitialFakeDelayMilliseconds();
    this.fakeDelayAck = applicationConfig.getInitialFakeDelayMilliseconds();
    this.fakeDelayTan = applicationConfig.getInitialFakeDelayMilliseconds();
    this.fakeDelayToken = applicationConfig.getInitialFakeDelayMilliseconds();
    this.movingAverageSampleSize = applicationConfig.getFakeDelayMovingAverageSamples();
  }

  /**
   * Returns the current fake delay after applying random jitter.
   */
  public long getJitteredFakeTanDelay() {
    return new PoissonDistribution(fakeDelayTan).sample();
  }

  /**
   * Returns the current fake delay after applying random jitter.
   */
  public long getJitteredFakeTestDelay() {
    return new PoissonDistribution(fakeDelayTest).sample();
  }

  /**
   * Returns the current fake delay after applying random jitter.
   */
  public long getJitteredFakeAckDelay() {
    return new PoissonDistribution(fakeDelayTest).sample();
  }

  /**
   * Returns the current fake delay after applying random jitter.
   */
  public long getJitteredFakeTokenDelay() {
    return new PoissonDistribution(fakeDelayToken).sample();
  }

  /**
   * Updates the moving average for the request duration for the Tan Endpoint with the specified value.
   */
  public void updateFakeTanRequestDelay(long realRequestDuration) {
    final long currentDelay = fakeDelayTan;
    fakeDelayTan = currentDelay + (realRequestDuration - currentDelay) / movingAverageSampleSize;
  }

  /**
   * Updates the moving average for the request duration for the Tan Endpoint with the specified value.
   */
  public void updateFakeTestRequestDelay(long realRequestDuration) {
    final long currentDelay = fakeDelayTest;
    fakeDelayTest = currentDelay + (realRequestDuration - currentDelay) / movingAverageSampleSize;
  }

  /**
   * Updates the moving average for the request duration for the Tan Endpoint with the specified value.
   */
  public void updateFakeAckRequestDelay(long realRequestDuration) {
    final long currentDelay = fakeDelayAck;
    fakeDelayAck = currentDelay + (realRequestDuration - currentDelay) / movingAverageSampleSize;
  }

  /**
   * Updates the moving average for the request duration for the Tan Endpoint with the specified value.
   */
  public void updateFakeTokenRequestDelay(long realRequestDuration) {
    final long currentDelay = fakeDelayToken;
    fakeDelayToken = currentDelay + (realRequestDuration - currentDelay) / movingAverageSampleSize;
  }

  /**
   * Returns the current fake delay in seconds. Used for monitoring.
   */
  public Double getFakeTanDelayInSeconds() {
    return fakeDelayTan / 1000.;
  }

  /**
   * Returns the current fake delay in seconds. Used for monitoring.
   */
  public Double getFakeTestDelayInSeconds() {
    return fakeDelayTest / 1000.;
  }

  /**
   * Returns the current fake delay in seconds. Used for monitoring.
   */
  public Double getFakeAckDelayInSeconds() {
    return fakeDelayAck / 1000.;
  }

  /**
   * Returns the current fake delay in seconds. Used for monitoring.
   */
  public Double getFakeTokenDelayInSeconds() {
    return fakeDelayToken / 1000.;
  }
}
