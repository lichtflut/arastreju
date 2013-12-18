/*
 * Copyright (C) 2013 lichtflut Forschungs- und Entwicklungsgesellschaft mbH
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.arastreju.sge.model.associations;

import org.arastreju.sge.context.Context;
import org.arastreju.sge.model.nodes.StatementOrigin;

import java.util.Date;

/**
 * <p>
 *  Statement meta info valid only in a given time period.
 * </p>
 *
 * <p>
 * 	Created Dec 18, 2013
 * </p>
 *
 * @author Oliver Tigges
 */
public class TimedStatementMetaInfo extends StatementMetaInfo {

    private Date validFrom;
    private Date validUntil;

    //-----------------------------------------------------

    public TimedStatementMetaInfo(Context[] contexts, Date timestamp, StatementOrigin origin, Date validFrom, Date validUntil) {
        super(contexts, timestamp, origin);
        this.validFrom = validFrom;
        this.validUntil = validUntil;
    }

    //-----------------------------------------------------

    public Date getValidFrom() {
        return validFrom;
    }

    public void setValidFrom(Date validFrom) {
        this.validFrom = validFrom;
    }

    public Date getValidUntil() {
        return validUntil;
    }

    public void setValidUntil(Date validUntil) {
        this.validUntil = validUntil;
    }
}
