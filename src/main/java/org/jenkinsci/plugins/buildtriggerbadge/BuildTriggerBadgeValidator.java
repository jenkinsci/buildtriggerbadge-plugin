/*
 *  The MIT License


 *
 *  Copyright 2010 Sony Ericsson Mobile Communications. All rights reserved.
 *
 *  Permission is hereby granted, free of charge, to any person obtaining a copy
 *  of this software and associated documentation files (the "Software"), to deal
 *  in the Software without restriction, including without limitation the rights
 *  to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 *  copies of the Software, and to permit persons to whom the Software is
 *  furnished to do so, subject to the following conditions:
 *
 *  The above copyright notice and this permission notice shall be included in
 *  all copies or substantial portions of the Software.
 *
 *  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 *  IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 *  FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 *  AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 *  LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 *  OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 *  THE SOFTWARE.
 */

package org.jenkinsci.plugins.buildtriggerbadge;

import hudson.ExtensionPoint;
import hudson.model.Cause;

import java.io.Serializable;

/**
 * Extension point for allowing disabling of the default icon, in case the class type of the Cause is more specialised
 * 
 * @author Ionut-Catalin Poitasu &lt;catalin.poitasu@gmail.com&gt;
 */

public abstract class BuildTriggerBadgeValidator implements Serializable, ExtensionPoint {
	/**
     * Method for acknowledging that another plug-in wants to handle the icon rendering functionality itself.
     *
     * @param cause Cause to use when verifying applicability
     * @return true if the plug-in provides its own icon to attach to a build, as a representation of a cause. This will disable the build-trigger-badge plugin for this cause.
     */
	
    public abstract boolean isApplicable(Cause cause);
}
