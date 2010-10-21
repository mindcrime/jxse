/*
 * Copyright (c) 2001-2007 Sun Microsystems, Inc. All rights reserved.
 *  
 *  The Sun Project JXTA(TM) Software License
 *  
 *  Redistribution and use in source and binary forms, with or without 
 *  modification, are permitted provided that the following conditions are met:
 *  
 *  1. Redistributions of source code must retain the above copyright notice,
 *     this list of conditions and the following disclaimer.
 *  
 *  2. Redistributions in binary form must reproduce the above copyright notice, 
 *     this list of conditions and the following disclaimer in the documentation 
 *     and/or other materials provided with the distribution.
 *  
 *  3. The end-user documentation included with the redistribution, if any, must 
 *     include the following acknowledgment: "This product includes software 
 *     developed by Sun Microsystems, Inc. for JXTA(TM) technology." 
 *     Alternately, this acknowledgment may appear in the software itself, if 
 *     and wherever such third-party acknowledgments normally appear.
 *  
 *  4. The names "Sun", "Sun Microsystems, Inc.", "JXTA" and "Project JXTA" must 
 *     not be used to endorse or promote products derived from this software 
 *     without prior written permission. For written permission, please contact 
 *     Project JXTA at http://www.jxta.org.
 *  
 *  5. Products derived from this software may not be called "JXTA", nor may 
 *     "JXTA" appear in their name, without prior written permission of Sun.
 *  
 *  THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED WARRANTIES,
 *  INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND 
 *  FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL SUN 
 *  MICROSYSTEMS OR ITS CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, 
 *  INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT 
 *  LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, 
 *  OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF 
 *  LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING 
 *  NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, 
 *  EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *  
 *  JXTA is a registered trademark of Sun Microsystems, Inc. in the United 
 *  States and other countries.
 *  
 *  Please see the license information page at :
 *  <http://www.jxta.org/project/www/license.html> for instructions on use of 
 *  the license in source files.
 *  
 *  ====================================================================
 *  
 *  This software consists of voluntary contributions made by many individuals 
 *  on behalf of Project JXTA. For more information on Project JXTA, please see 
 *  http://www.jxta.org.
 *  
 *  This license is based on the BSD license adopted by the Apache Foundation. 
 */
package net.jxta.test.util;


import net.jxta.document.AdvertisementFactory;
import net.jxta.document.MimeMediaType;
import net.jxta.document.StructuredDocument;
import net.jxta.document.StructuredDocumentFactory;
import net.jxta.document.StructuredDocumentUtils;
import net.jxta.id.IDFactory;
import net.jxta.id.TestIDFactory;
import net.jxta.impl.protocol.MulticastAdv;
import net.jxta.peergroup.PeerGroup;
import net.jxta.peergroup.PeerGroupID;
import net.jxta.peer.PeerID;
import net.jxta.protocol.PeerAdvertisement;
import net.jxta.impl.protocol.TCPAdv;


/**
 *  PeerAdvUtil is a utility to ease the generation of peer advertisements
 */
public class AdvUtil {

    /**
     *  creates a new PeerAdvertisement with a specificed name and tcp address
     *
     *@param  name         Peer Name
     *@param  inetAddress  String representation of the tcp address
     *@param  port         Inetaddress as a string
     *@return              new Peer advertisement with specified name, and tcp
     *      address
     */
    public static PeerAdvertisement newPeerAdv(String name, String inetAddress, int port, boolean incoming) {

        PeerAdvertisement peerAdvertisement = null;

        try {
            peerAdvertisement = (PeerAdvertisement)
                    AdvertisementFactory.newAdvertisement(PeerAdvertisement.getAdvertisementType());
            PeerID pid = TestIDFactory.newPeerID(PeerGroupID.defaultNetPeerGroupID);

            peerAdvertisement.setPeerID(pid);
            peerAdvertisement.setName(name);
            peerAdvertisement.setPeerGroupID(PeerGroupID.defaultNetPeerGroupID);
            TCPAdv tcpAdv = (TCPAdv) AdvertisementFactory.newAdvertisement(TCPAdv.getAdvertisementType());
            MulticastAdv multicastAdv = (MulticastAdv) AdvertisementFactory.newAdvertisement(MulticastAdv.getAdvertisementType());

            tcpAdv.setProtocol("TCP");
            tcpAdv.setPort(port);
            multicastAdv.setMulticastAddr("224.0.1.85");
            multicastAdv.setMulticastPort(1234);
            multicastAdv.setMulticastSize(16384);
            multicastAdv.setMulticastState(true);
            if (incoming) {
                tcpAdv.setServer(inetAddress + ":" + port);
            }
            tcpAdv.setInterfaceAddress(inetAddress);
            StructuredDocument tcp = StructuredDocumentFactory.newStructuredDocument(MimeMediaType.XMLUTF8, "Parm");

            StructuredDocumentUtils.copyElements(tcp, tcp, (StructuredDocument)
                    tcpAdv.getDocument(MimeMediaType.XMLUTF8));
            peerAdvertisement.putServiceParam(PeerGroup.tcpProtoClassID, tcp);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return peerAdvertisement;
    }

}

