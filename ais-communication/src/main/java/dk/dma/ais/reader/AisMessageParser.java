/* Copyright (c) 2011 Danish Maritime Safety Administration
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this library.  If not, see <http://www.gnu.org/licenses/>.
 */
package dk.dma.ais.reader;

import java.util.concurrent.atomic.AtomicReference;

import dk.dma.ais.message.AisMessage;
import dk.dma.enav.messaging.MaritimeMessageHandler;
import dk.dma.enav.messaging.MaritimeMessageMetadata;

/**
 * Move this to AisMessage#tryParse(String) when cleaned up
 * 
 */
public class AisMessageParser {

    /**
     * Parses a (Should the signature be String[]??)
     * 
     * @param string
     *            the string to parse
     * @return the corresponding AIS message or null if the string could not be parsed
     */
    public static AisMessage tryParse(String string) {
        final AtomicReference<AisMessage> ref = new AtomicReference<>();
        if (string != null && string.length() > 0) {
            DummyReader dummy = new DummyReader();

            dummy.registerHandler(new MaritimeMessageHandler<AisMessage>() {

                public void handle(AisMessage message, MaritimeMessageMetadata metadata) {
                    ref.set(message);
                }
            });

            String lines[] = string.split("\\r?\\n");
            for (String s : lines) {
                dummy.handleLine(s);
            }
        }
        return ref.get();
    }

    static class DummyReader extends AisReader {
        /** {@inheritDoc} */
        @Override
        protected void handleLine(String line) {
            super.handleLine(line);
        }

        @Override
        public void send(SendRequest sendRequest, ISendResultListener resultListener) throws SendException {}

        @Override
        public Status getStatus() {
            return null;
        }

        @Override
        public void close() {}
    }
}
