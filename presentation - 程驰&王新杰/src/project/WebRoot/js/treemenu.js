function Folder(folderDescription, hreference) {
   //constant data;
   this.desc = folderDescription;
   this.hreference = hreference;
   this.id = -1;
   this.navObj = 0;
   this.iconImg = 0;
   this.nodeImg = 0;
   this.isLastNode = 0;

   // dynamic data
   this.isRoot = true;
   this.isOpen = true;
   this.iconSrc = base+"/images/ftv2folderopen.gif";
   this.iconRoot = base+"/images/ftv2folderopen.gif"
   this.children = new Array;
   this.nChildren = 0;

   // methods
   this.initialize = initializeFolder;
   this.setState = setStateFolder;
   this.addChild = addChild;
   this.createIndex = createEntryIndex;
   this.hide = hideFolder;
   this.display = display;
   this.renderOb = drawFolder;
   this.totalHeight = totalHeight;
   this.subEntries = folderSubEntries;
   this.outputLink = outputFolderLink;
}


function setStateFolder(isOpen) {
   var subEntries;
   var totalHeight;
   var fIt = 0;
   var i = 0;

   if(isOpen == this.isOpen)
       return;

   if(browserVersion == 2) {
       totalHeight = 0
       for(i = 0; i < this.nChildren; i++)
           totalHeight = totalHeight + this.children[i].navObj.clip.height;
       subEntries = this.subEntries();

       if(this.isOpen)
           totalHeight = 0 - totalHeight;

       for(fIt = this.id + subEntries + 1; fIt < nEntries; fIt++)
           indexOfEntries[fIt].navObj.moveBy(0, totalHeight);
   }

   this.isOpen = isOpen;
   propagateChangesInState(this);
}


function propagateChangesInState(folder) {
   var i = 0;
   if(folder.isOpen) {
       if(folder.nodeImg) {
           if(folder.isLastNode)
               folder.nodeImg.src = base+"/images/ftv2mlastnode.gif";
           else
               folder.nodeImg.src = base+"/images/ftv2mnode.gif";
       }

       if(this.isRoot)
           folder.iconImg.src = base+"/images/ftv2_mail.gif";
       else
           folder.iconImg.src = base+"/images/ftv2folderopen.gif";

       for(i = 0; i < folder.nChildren; i++)
           folder.children[i].display();
   }
   else {
       if(folder.nodeImg) {
           if(folder.isLastNode)
               folder.nodeImg.src = base+"/images/ftv2plastnode.gif";
           else
               folder.nodeImg.src = base+"/images/ftv2pnode.gif";
       }

       if(this.isRoot)
           folder.iconImg.src = base+"/images/ftv2_mail.gif";
       else
           folder.iconImg.src = base+"/images/ftv2folderclosed.gif";

       for(i = 0; i < folder.nChildren; i++)
           folder.children[i].hide();
   }
}

function hideFolder() {
    if(browserVersion == 1) {
        if(this.navObj.style.display == "none")
            return;

        this.navObj.style.display = "none";
    }
    else {
        if(this.navObj.visibility == "hiden")
            return;

        this.navObj.visibility = "hiden";
    }

    this.setState(0);
}


function initializeFolder(level, lastNode, leftSide) {
    var i = 0;
    var j = 0;

    var numberOfFolders;
    var numberOfDocs;

    nc = this.nChildren;
    this.createIndex();

    var nc;
    var auxEv = "";

    if(browserVersion > 0)
        auxEv = "<A HREF='JavaScript: clickOnNode("+this.id+")'>";
    else
        auxEv = "<A>";

    if(level > 0) {
        if(lastNode) { //the last 'brother' in the children array
           this.renderOb(leftSide + auxEv + "<IMG NAME='nodeIcon" + this.id + "' SRC='"+base+"/images//ftv2mlastnode.gif' WIDTH=16 HEIGHT=22 BORDER=0></A>");
           leftSide = leftSide + "<IMG SRC='"+base+"/images/ftv2blank.gif' WIDTH=16 HEIGHT=22>";
           this.isLastNode = 1;
        }
        else {
            this.renderOb(leftSide + auxEv + "<IMG NAME='nodeIcon" + this.id + "' SRC='"+base+"/images//ftv2mnode.gif' WIDTH=16 HEIGHT=22 BORDER=0></A>");
            leftSide = leftSide + "<IMG SRC='"+base+"/images/ftv2vertline.gif' WIDTH=16 HEIGHT=22>";
            this.isLastNode = 0;
        }
    }
    else {
        this.renderOb("");
    }

    if(nc > 0) {
        level = level + 1;
        for(i = 0; i < this.nChildren; i++) {
            if(i == this.nChildren-1)
                this.children[i].initialize(level, 1, leftSide);
            else
                this.children[i].initialize(level, 0, leftSide);
        }
    }
}


function drawFolder(leftSide) {
    //alert(leftSide);
    if(browserVersion == 2) {
        if(!doc.yPos)
          doc.yPos = 8;

        doc.write("<LAYER ID='folder" + this.id + "' TOP=" + doc.yPos + " VISIBILITY=hiden>");
    }
    doc.write("<TABLE")

    if(browserVersion == 1)
        doc.write(" ID='folder" + this.id + "' STYLE='position:block;' ");

    doc.write(" BORDER=0 CELLSPACING=0 CELLPADDING=0>");
    doc.write("<TR><TD>");
    doc.write(leftSide);

    this.outputLink();

    doc.write("<IMG NAME='folderIcon" + this.id + "' ");
    if(leftSide == '') {
        doc.write("SRC='" + this.iconRoot+"' BORDER=0></A>");
    }
    else {
        doc.write("SRC='" + this.iconSrc+"' BORDER=0></A>");
    }

    doc.write("</TD><TD NOWRAP>");

    doc.write("<DIV CLASS=\"fldrroot\">");

    if(leftSide == '') {
       if(USETEXTLINKS) {
          this.outputLink();
          doc.write(this.desc + "</A>");
       }
       else
         doc.write(this.desc);
    }
    else {
        doc.write("<A HREF='JavaScript: clickOnNode("+this.id+")'>");
        doc.write(this.desc + "</A>");
    }

    doc.write("</DIV>");
    doc.write("</TD>");
    doc.write("</TABLE>");

    if(browserVersion == 2) {
        doc.write("</LAYER>");
    }

    if(browserVersion == 1) {
        this.navObj = doc.all["folder"+this.id];
        this.iconImg = doc.all["folderIcon"+this.id]
        this.nodeImg = doc.all["nodeIcon"+this.id]
    }
    else if(browserVersion == 2) {
        this.navObj = doc.layers["folder"+this.id];
        this.iconImg = this.navObj.document.images["folderIcon"+this.id];
        this.nodeImg = this.navObj.document.images["nodeIcon"+this.id];
        doc.yPos = doc.yPos + this.navObj.clip.height;
    }
}


function outputFolderLink() {
    if(this.hreference) {
        doc.write("<A HREF='" + this.hreference + "' TARGET=\"right_frame\" ")

        if(browserVersion > 0) {
            doc.write("onClick='JavaScript: clickOnFolder("+this.id+")'")
        }
        doc.write(">")
    }
    else
        doc.write("<A>")
}


function addChild(childNode) {
    this.children[this.nChildren] = childNode;
    this.nChildren++;

    return(childNode);
}


function folderSubEntries() {
    var i = 0;
    var se = this.nChildren;

    for(i = 0; i < this.nChildren; i++) {
        if(this.children[i].children) //is a folder
            se = se + this.children[i].subEntries();
    }

    return(se)
}


// Definition of class Item (a document or link inside a Folder)

function Item(itemDescription, hreference, itemLink, itemImg) // Constructor
{
    // constant data
    this.desc = itemDescription
    this.link = itemLink
    this.hreference = hreference;
    this.id = -1;     //initialized in initalize()
    this.navObj = 0;  //initialized in render()
    this.iconImg = 0; //initialized in render()

    // iconSrc�� ��d�Ǵ� �̹��� ����; �� �����ۿ� �°� ��d�� �� �ֵ��� �Ѵ� (��ǥ)
    this.iconSrc = itemImg;

    // methods
    this.initialize = initializeItem ;
    this.createIndex = createEntryIndex;
    this.hide = hideItem;
    this.display = display;
    this.renderOb = drawItem;
    this.totalHeight = totalHeight;
}


function hideItem() {
    if(browserVersion == 1) {
        if(this.navObj.style.display == "none")
            return;
        this.navObj.style.display = "none"
    }
    else {
        if(this.navObj.visibility == "hiden")
            return;
        this.navObj.visibility = "hiden";
    }
}


function initializeItem(level, lastNode, leftSide) {
    this.createIndex();

    if(level > 0) {
        if(lastNode) { //the last 'brother' in the children array
            this.renderOb(leftSide + "<IMG SRC='"+base+"/images/ftv2lastnode.gif' WIDTH=16 HEIGHT=22>")
            leftSide = leftSide + "<IMG SRC='"+base+"/images/ftv2blank.gif' WIDTH=16 HEIGHT=22>"
        }
        else {
            this.renderOb(leftSide + "<IMG SRC='"+base+"/images/ftv2node.gif' WIDTH=16 HEIGHT=22>")
            leftSide = leftSide + "<IMG SRC='"+base+"/images/ftv2vertline.gif' WIDTH=16 HEIGHT=22>"
        }
    }
    else
        this.renderOb("")
}


function drawItem(leftSide) {
    if(browserVersion == 2)
        doc.write("<LAYER ID='item" + this.id + "' TOP=" + doc.yPos + " VISIBILITY=hiden>");

    doc.write("<TABLE ");
    if(browserVersion == 1)
        doc.write(" ID='item" + this.id + "' STYLE='position:block;' ");
    doc.write(" BORDER=0 CELLSPACING=0 CELLPADDING=0>");
    doc.write("<TR><TD>");
    doc.write(leftSide);

    if(this.link != "")
        doc.write("<A HREF=" + this.link + ">");

    doc.write("<IMG ID='itemIcon"+this.id+"' ")
    doc.write("SRC='"+this.iconSrc+"' BORDER=0>")

    if(this.link != "")
        doc.write("</A>");

    doc.write("</TD><TD NOWRAP>");
    doc.write("<DIV CLASS=\"fldritem\">");
    if(USETEXTLINKS) {
        if(this.link != "")
            doc.write("<A HREF=" + this.link + ">" + this.desc + "</A>");
        else
            doc.write(this.desc);
    }
    else {
        doc.write(this.desc);
    }

    doc.write("</DIV>");
    doc.write("</TABLE>")

    if(browserVersion == 2)
        doc.write("</layer>");

    if(browserVersion == 1) {
        this.navObj = doc.all["item"+this.id]
        this.iconImg = doc.all["itemIcon"+this.id]
    }
    else if(browserVersion == 2) {
        this.navObj = doc.layers["item"+this.id];
        this.iconImg = this.navObj.document.images["itemIcon"+this.id];
        doc.yPos = doc.yPos+this.navObj.clip.height;
    }
}


// Methods common to both objects (pseudo-inheritance)
function display() {
    if(browserVersion == 1)
        this.navObj.style.display = "block";
    else
        this.navObj.visibility = "show";
}


function createEntryIndex() {
    this.id = nEntries;
    indexOfEntries[nEntries] = this;
    nEntries++;
}


// total height of subEntries open
function totalHeight() { //used with browserVersion == 2
    var h = this.navObj.clip.height;
    var i = 0;

    if(this.isOpen) { // is a folder and _is_ open
        for(i = 0 ; i < this.nChildren; i++)
            h = h + this.children[i].totalHeight();
    }

    return h
}


// Events

function clickOnFolder(folderId) {
    var clicked = indexOfEntries[folderId];

    if(!clicked.isOpen)
        clickOnNode(folderId);

    if(clicked.isSelected)
        return;
}


function clickOnNode(folderId) {
    var clickedFolder = 0;
    var state = 0;

    clickedFolder = indexOfEntries[folderId];
    state = clickedFolder.isOpen;

    clickedFolder.setState(!state); //open<->close
}


function initializeDocument(Num) {
    if(doc.all)
        browserVersion = 1; //IE4
    else if(doc.layers)
        browserVersion = 2; //NS4
    else
        browserVersion = 0; //other

    foldersTree.initialize(0, 1, "");
    foldersTree.display();

    if(browserVersion > 0) {
        doc.write("<LAYER TOP="+indexOfEntries[nEntries-1].navObj.top+">&nbsp;</LAYER>");

        // close the whole tree
        clickOnNode(Num);

        // open the root folder
        clickOnNode(Num);
    }
}
function initializeDocument2(Num) {
    if(doc.all)
        browserVersion = 1; //IE4
    else if(doc.layers)
        browserVersion = 2; //NS4
    else
        browserVersion = 0; //other

    MenuTree.initialize(0, 1, "");
    MenuTree.display();

    if(browserVersion > 0) {
        doc.write("<LAYER TOP="+indexOfEntries[nEntries-1].navObj.top+">&nbsp;</LAYER>");

        // close the whole tree
        clickOnNode(Num);

        //open the root folder
        clickOnNode(Num);
    }
}
// Auxiliary Functions for Folder-Treee backward compatibility
function gFldr(description, hreference) {
    folder = new Folder(description, hreference);
    return(folder);
}


function gLnk(target, description, linkData, itemImg) {
    fullLink = ""

    if(target == 0) {
        if(linkData != "")
            fullLink = "'"+linkData+"' TARGET=\"right_frame\"";
        else
            fullLink = "";
    }
    else if(target == 1) {
        if(linkData != "")
            fullLink = "'"+linkData+"' TARGET=\"_top\"";
        else
            fullLink = "";
    }
    else { // target == 2
        if(linkData != "")
            fullLink = "'http://"+linkData+"' TARGET=\"right_frame\"";
        else
            fullLink = "";
    }

    linkItem = new Item(description, linkData, fullLink, itemImg)
    return linkItem
}


function insFldr(parentFolder, childFolder) {
    return(parentFolder.addChild(childFolder));
}


function insDoc(parentFolder, document) {
    parentFolder.addChild(document);
}


// Global variables

USETEXTLINKS = 1;
indexOfEntries = new Array;
nEntries = 0;
doc = document;
browserVersion = 0;
selectedFolder = 0;