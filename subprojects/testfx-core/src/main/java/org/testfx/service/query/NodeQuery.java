/*
 * Copyright 2013-2014 SmartBear Software
 *
 * Licensed under the EUPL, Version 1.1 or - as soon they will be approved by the European
 * Commission - subsequent versions of the EUPL (the "Licence"); You may not use this work
 * except in compliance with the Licence.
 *
 * You may obtain a copy of the Licence at:
 * http://ec.europa.eu/idabc/eupl
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the
 * Licence is distributed on an "AS IS" basis, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. See the Licence for the specific language governing permissions
 * and limitations under the Licence.
 */
package org.testfx.service.query;

import java.util.Set;
import javafx.scene.Node;

import com.google.common.base.Function;
import com.google.common.base.Optional;
import com.google.common.base.Predicate;

public interface NodeQuery {

    //---------------------------------------------------------------------------------------------
    // METHODS.
    //---------------------------------------------------------------------------------------------

    public NodeQuery from(Node... parentNodes);

    public NodeQuery from(Set<Node> parentNodes);

    public NodeQuery lookup(Function<Node, Set<Node>> selector);

    public NodeQuery lookupAt(int index,
                              Function<Node, Set<Node>> selector);

    public NodeQuery match(Predicate<Node> filter);

    public NodeQuery throwIfEmpty();

    public Set<Node> queryAll();

    public Optional<Node> queryFirst();

}
