// Pooi (c) 2008-2018 MIT License Baltasar <jbgarcia@uvigo.es>
package com.devbaltasarq.pooi.ui;

import com.devbaltasarq.pooi.core.AppInfo;
import com.devbaltasarq.pooi.core.ObjectBag;
import com.devbaltasarq.pooi.core.Runtime;
import com.devbaltasarq.pooi.core.evaluables.Attribute;
import com.devbaltasarq.pooi.core.evaluables.Method;
import com.devbaltasarq.pooi.core.evaluables.ParentAttribute;
import com.devbaltasarq.pooi.core.objs.ObjectParent;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;

/**
 * Created by Baltasar on 30/04/2016.
 */
public final class Inspector extends JDialog {
    private static final String EtqIconCopy = "icons/copy.png";
    private static final String EtqIconAddMethod = "icons/addMethod.png";
    private static final String EtqIconAddAttribute = "icons/addAttribute.png";
    private static final String EtqIconExecute = "icons/execute.png";
    private static final String EtqIconDelete = "icons/delete.png";

    public Inspector(VisualEngine parentWindow, ObjectBag obj) {
        this.beingBuilt = true;
        this.visualEngine = parentWindow;
        this.rt = parentWindow.getInterpreter().getRuntime();
        this.objInheritanceRoot = this.rt.getAbsoluteParent();
        this.obj = obj;
        this.objPath = obj.getPath();
        this.setIconImage( parentWindow.getIconImage() );
        this.setFont( this.visualEngine.getFont() );
        this.build();
        this.beingBuilt = false;
        this.setVisible( true );
    }

    /** Retries icons from jar for future use */
    private void buildIcons()
    {
        URL url;

        try {
            url = this.getClass().getClassLoader().getResource( EtqIconCopy );
            this.iconCopy = new ImageIcon( url );

            url = this.getClass().getClassLoader().getResource( EtqIconAddMethod );
            this.iconAddMethod = new ImageIcon( url );

            url = this.getClass().getClassLoader().getResource( EtqIconAddAttribute );
            this.iconAddAttribute = new ImageIcon( url );

            url = this.getClass().getClassLoader().getResource( EtqIconExecute );
            this.iconExecute = new ImageIcon( url );

            url = this.getClass().getClassLoader().getResource( EtqIconDelete );
            this.iconDelete = new ImageIcon( url );

        } catch(Exception exc)
        {
            this.visualEngine.makeOutput( "Warning: failed to retrieve icons from jar\n\n" );
        }
    }

    private void buildCloseButton()
    {
        final JButton btClose = new JButton( "Close" );

        btClose.addActionListener( (e) -> Inspector.this.finish() );

        JPanel pnlClose = new JPanel();
        FlowLayout flow = new FlowLayout();

        pnlClose.add( btClose );
        flow.setAlignment( FlowLayout.RIGHT );
        pnlClose.setLayout( flow );
        this.add( pnlClose, BorderLayout.PAGE_END );
    }

    private void buildBasicAttrsPanel()
    {
        // Add panel for object's name
        final JLabel lblObjectName = new JLabel( "Name" );
        final JPanel pnlName = new JPanel();
        pnlName.setBorder( new EmptyBorder( 10, 10, 10, 10 ) );
        pnlName.setLayout( new BoxLayout( pnlName, BoxLayout.LINE_AXIS ) );
        pnlName.add( lblObjectName );
        pnlName.add( Box.createRigidArea( new Dimension( 10, 0 ) ) );

        this.edObjectName = new JTextField();
        this.edObjectName.setHorizontalAlignment( JTextField.RIGHT );
        pnlName.add( edObjectName );
        this.pnlAction.add( pnlName );
        this.pnlAction.add( Box.createVerticalGlue() );

        // Object's name events
        this.edObjectName.addFocusListener( new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {}

            @Override
            public void focusLost(FocusEvent e) {
                Inspector.this.onEditObjectName();
            }
        } );

        this.edObjectName.addActionListener( (e) -> Inspector.this.onEditObjectName() );

        // Add panel for parent attribute
        if ( !( this.getObj() instanceof ObjectParent ) ) {
            final JLabel lblParentAttributeName = new JLabel( "Parent" );
            final JPanel pnlAttrParent = new JPanel();

            this.cbParent = new JComboBox( this.availableObjectsNames );
            ( (JLabel) this.cbParent.getRenderer() ).setHorizontalAlignment( JLabel.RIGHT );
            ( (JTextField) this.cbParent.getEditor().getEditorComponent() ).setHorizontalAlignment( JTextField.RIGHT  );
            this.cbParent.setFont( this.visualEngine.getFont() );
            this.cbParent.setEditable( true );
            this.cbParent.getModel().setSelectedItem( this.getObj().getParentObject().getNameOrValueAsString() );

            pnlAttrParent.setBorder( new EmptyBorder( 10, 10, 10, 10 ) );
            pnlAttrParent.setLayout( new BoxLayout( pnlAttrParent, BoxLayout.LINE_AXIS ) );
            pnlAttrParent.add( lblParentAttributeName );
            pnlAttrParent.add( Box.createRigidArea( new Dimension( 10, 0 ) ) );
            pnlAttrParent.add( this.cbParent );
            this.pnlAction.add( pnlAttrParent );
            this.pnlAction.add( Box.createVerticalGlue() );

            // Combobox's events for the parent attribute
            this.cbParent.addFocusListener( new FocusListener() {
                @Override
                public void focusGained(FocusEvent e) {}

                @Override
                public void focusLost(FocusEvent e) {
                    Inspector.this.onChangeParent();
                }
            } );
            this.cbParent.addActionListener( (e) -> Inspector.this.onChangeParent() );
        }
    }

    private void buildManagementButtonsPanel()
    {
        final JPanel pnlButtons = new JPanel();
        final JButton btAddAttribute = Util.createButton( this.iconAddAttribute,  "Add attribute" );
        final JButton btAddMethod = Util.createButton( this.iconAddMethod, "Add method" );
        final JButton btCopy = Util.createButton( this.iconCopy, "Copy" );

        pnlButtons.setBorder( new EmptyBorder( 10, 10, 10, 10 ) );
        pnlButtons.setLayout( new BoxLayout( pnlButtons, BoxLayout.LINE_AXIS ) );
        pnlButtons.add( btAddAttribute );
        pnlButtons.add( Box.createHorizontalGlue() );
        pnlButtons.add( btAddMethod );
        pnlButtons.add( Box.createHorizontalGlue() );
        pnlButtons.add( btCopy );

        this.pnlAction.add( pnlButtons );
        this.pnlAction.add( Box.createVerticalGlue() );

        btAddAttribute.addActionListener( (e) -> Inspector.this.onAddAttribute() );
        btAddMethod.addActionListener( (e) -> Inspector.this.onAddMethod() );
        btCopy.addActionListener( (e) -> Inspector.this.copyObject() );
    }

    private void addAttributePanel(Attribute attr)
    {
        final ObjectBag objDest = attr.getReference();
        final JTextField edName = new JTextField( attr.getName() );
        final JButton btDelete = Util.createButton( this.iconDelete, "Delete" );
        final JPanel panel = new JPanel();

        panel.setBorder(new EmptyBorder( 10, 10, 10, 10 ) );
        panel.setLayout( new BoxLayout( panel, BoxLayout.LINE_AXIS ) );


        final JComboBox cbContents = new JComboBox( this.availableObjectsNames );
        ( (JLabel) cbContents.getRenderer() ).setHorizontalAlignment( JLabel.RIGHT );
        ( (JTextField) cbContents.getEditor().getEditorComponent() ).setHorizontalAlignment( JTextField.RIGHT  );
        cbContents.setFont( this.visualEngine.getFont() );
        cbContents.setEditable( true );
        cbContents.getModel().setSelectedItem( objDest.getNameOrValueAsString() );

        panel.add( btDelete );
        panel.add( Box.createRigidArea( new Dimension( 10, 0 ) ) );
        panel.add( edName );
        panel.add( Box.createRigidArea( new Dimension( 10, 0 ) ) );
        panel.add( cbContents );

        this.pnlAttributes.add( panel );
        this.pnlAttributes.add( Box.createVerticalGlue() );

        // Events for the name of the attribute
        edName.addFocusListener( new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {}

            @Override
            public void focusLost(FocusEvent e) {
                Inspector.this.onRenameAttribute( edName, attr );
            }
        } );

        edName.addActionListener( (e) -> Inspector.this.onRenameAttribute( edName, attr ) );

        // Events for the contents of the attribute
        cbContents.addFocusListener( new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {}

            @Override
            public void focusLost(FocusEvent e) {
                Inspector.this.onChangeContentsForAttribute( cbContents, attr );
            }
        } );

        cbContents.addActionListener( (e) -> Inspector.this.onChangeContentsForAttribute( cbContents, attr ) );

        btDelete.addActionListener( (e) -> Inspector.this.onDeleteAttribute( panel, attr ) );
    }

    private void buildAttributesManagementPanel()
    {
        final HashSet<String> attrIds = new HashSet<>( this.getObj().getNumberOfMethods() );

        this.pnlAttributes = new JPanel();
        final JScrollPane scroll = new JScrollPane( this.pnlAttributes );

        this.pnlAttributes.setLayout( new BoxLayout( this.pnlAttributes, BoxLayout.PAGE_AXIS ) );
        this.pnlAction.add( scroll );
        this.pnlAction.add( Box.createVerticalGlue() );

        for(Attribute attr: this.getObj().getAttributes()) {
            if ( attr instanceof ParentAttribute ) {
                continue;
            }

            String attrName = attr.getName();

            if ( attrIds.contains( attrName ) ) {
                continue;
            } else {
                attrIds.add( attrName );
            }

           this.addAttributePanel( attr );
        }
    }

    private void addMethodPanel(ObjectBag.Slot slot)
    {
        String mthName = slot.getMethod().getName();
        final JPanel panel = new JPanel();
        panel.setBorder( new EmptyBorder( 10, 10, 10, 10 ) );
        panel.setLayout( new BoxLayout( panel, BoxLayout.LINE_AXIS ) );

        JButton btDelete = Util.createButton( this.iconDelete, "Delete" );
        JButton btExecute = Util.createButton( this.iconExecute, "Execute" );
        JTextField edName = new JTextField( mthName );
        JTextField edContents = new JTextField( slot.getMethod().getMethodBodyAsString() );
        edContents.setHorizontalAlignment( JTextField.RIGHT );
        panel.add( btExecute );
        panel.add( Box.createRigidArea( new Dimension( 10, 0 ) ) );
        panel.add( btDelete );
        panel.add( Box.createRigidArea( new Dimension( 10, 0 ) ) );
        panel.add( edName );
        panel.add( Box.createRigidArea( new Dimension( 10, 0 ) ) );
        panel.add( edContents );
        this.pnlMethods.add( panel );
        this.pnlMethods.add( Box.createVerticalGlue() );

        // Events for the name of the method
        edName.addFocusListener( new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {}

            @Override
            public void focusLost(FocusEvent e) {
                Inspector.this.onRenameMethod( slot, edName );
            }
        } );

        edName.addActionListener( (e) -> Inspector.this.onRenameMethod( slot, edName ) );

        // Events for the body of the method
        edContents.addFocusListener( new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {}

            @Override
            public void focusLost(FocusEvent e) {
                Inspector.this.onMethodBodyChanged( edContents, slot );
            }
        } );

        edContents.addActionListener( (e) -> Inspector.this.onMethodBodyChanged( edContents, slot ) );

        // Events for buttons
        btExecute.addActionListener( (e) -> {
                if ( !Inspector.this.beingBuilt ) {
                    Inspector.this.launchMethodExecution( slot.getMethod().getName() );
                }
        });

        btDelete.addActionListener( (e) -> Inspector.this.onDeleteMethod( panel, slot ) );
    }

    private void buildMethodsManagementPanel() {
        this.pnlMethods = new JPanel();
        JScrollPane scroll = new JScrollPane( this.pnlMethods );
        this.pnlMethods.setLayout( new BoxLayout( this.pnlMethods, BoxLayout.PAGE_AXIS ) );
        this.pnlAction.add( scroll );
        this.pnlAction.add( Box.createVerticalGlue() );

        HashSet<String> mthIds = new HashSet<>( this.getObj().getNumberOfMethods() );
        ObjectBag obj = this.getObj();
        do {
            for (Method mth : obj.getLocalMethods()) {
                final String mthName = mth.getName();

                if ( mthIds.contains( mthName ) ) {
                    continue;
                } else {
                    mthIds.add( mthName );
                }

                this.addMethodPanel( obj.lookUpSlot( mthName ) );
            }

            obj = obj.getParentObject();
        } while( obj != objInheritanceRoot
              && obj != null );
    }

    private void buildActionArea()
    {
        // Create a box layout in the center
        this.pnlAction = new JPanel();
        JScrollPane scroll = new JScrollPane( this.pnlAction );
        this.pnlAction.setLayout( new BoxLayout( this.pnlAction, BoxLayout.PAGE_AXIS ) );
        this.add( scroll, BorderLayout.CENTER );

        this.buildBasicAttrsPanel();
        this.buildManagementButtonsPanel();
        this.buildAttributesManagementPanel();
        this.buildMethodsManagementPanel();
    }

    private void build()
    {
        this.buildAvailableObjectsNames();

        // Build
        this.setLayout( new BorderLayout( 5, 5 ) );
        this.buildIcons();
        this.buildCloseButton();
        this.buildActionArea();

        // Configure
        this.setDefaultCloseOperation( JFrame.DISPOSE_ON_CLOSE );
        this.setModal( true );
        this.pack();
        this.updateObjectBasicAttributes();
        this.setMinimumSize( new Dimension( 300, 200 ) );

        // Resize and put it in its location
        Point p = new Point( this.visualEngine.getLocation() );
        p.x += ( this.visualEngine.getWidth() - this.getWidth() ) / 2;
        p.y += ( this.visualEngine.getHeight() - this.getHeight() ) / 2;
        this.setLocation( p );
    }

    private void updateObjectBasicAttributes()
    {
        ObjectBag obj = this.getObj();
        ObjectBag objParent = this.getObj().getParentObject();

        if ( obj != null ) {
            this.setTitle( this.getObj().getPath() + " - Inspector" );
            this.edObjectName.setText( this.getObj().getName() );
        } else {
            this.setTitle( "Inspector" );
        }

        if ( objParent != null
          && !( obj instanceof ObjectParent ) )
        {
            this.cbParent.setSelectedItem( objParent.getName() );
        }
    }

    private void launchMethodExecution(String methodName)
    {
        String arguments = "";
        Method mth = this.getObj().lookUpMethod( methodName );

        if ( mth.getNumParams() > 0 ) {
            arguments = (String) JOptionPane.showInputDialog(
                    this,
                    "Method's arguments:",
                    AppInfo.Name,
                    JOptionPane.PLAIN_MESSAGE,
                    null,
                    null,
                    "0" );

            if ( arguments == null ) {
                arguments = "";
            } else {
                arguments = arguments.trim();
            }
        }

        this.visualEngine.execute( this.objPath + " " + methodName + " " + arguments );
        this.finish();
    }

    private void copyObject()
    {
        String newName = (String) JOptionPane.showInputDialog(
                this,
                "New object's name:",
                AppInfo.Name,
                JOptionPane.PLAIN_MESSAGE,
                null,
                null,
                "obj" + ( this.visualEngine.getInterpreter().getRuntime().getRoot().getNumberOfAttributes() + 1 ) );

        if ( newName != null ) {
            newName = newName.trim();
            if ( newName.length() > 0 ) {
                this.visualEngine.execute( "(" + this.objPath + " copy) rename \"" + newName + '\"' );
                this.finish();
            }
        }
    }

    public ObjectBag getObj()
    {
        return this.obj;
    }

    static void addAttribute(Window frame, VisualEngine visualEngine, ObjectBag obj)
    {
        String attrName = (String) JOptionPane.showInputDialog(
                frame,
                "New attribute's name:",
                AppInfo.Name,
                JOptionPane.PLAIN_MESSAGE,
                null,
                null,
                "x" );

        if ( attrName != null ) {
            attrName = attrName.trim();

            if ( attrName.length() > 0 ) {
                visualEngine.execute( obj.getPath() + "." + attrName + " = 0" );
            }
        }
    }

    static void addMethod(Window frame, VisualEngine visualEngine, ObjectBag obj)
    {
        String mthName = (String) JOptionPane.showInputDialog(
                frame,
                "New method's name:",
                AppInfo.Name,
                JOptionPane.PLAIN_MESSAGE,
                null,
                null,
                "f" );

        if ( mthName != null ) {
            mthName = mthName.trim();
            if ( mthName.length() > 0 ) {
                changeMethod( frame, visualEngine, obj, mthName, "{:}" );
            }
        }

        return;
    }

    static void changeMethod(Window frame, VisualEngine visualEngine, ObjectBag obj, String name, String body)
    {
        String mthBody = (String) JOptionPane.showInputDialog(
                frame,
                name + "'s body:",
                AppInfo.Name,
                JOptionPane.PLAIN_MESSAGE,
                null,
                null,
                body );

        if ( mthBody != null ) {
            mthBody = mthBody.trim();
            if ( mthBody.length() > 0 ) {
                visualEngine.execute( obj.getPath() + "." + name + " = " + mthBody );
            }

        }

        return;
    }

    /** Compiles all the names in the Root object and stores then in the String[] array
      * this.availableObjectNames
      */
    private void buildAvailableObjectsNames()
    {
        final ArrayList<String> objNames = new ArrayList<>();
        final Object[] names = Arrays.stream(
                                        this.rt.getRoot().getAttributes() )
                                    .map( Attribute::getName ).toArray();


        Arrays.stream( names ).forEach( (x) -> objNames.add( x.toString() ) );
        this.availableObjectsNames = new String[ objNames.size() ];
        objNames.toArray( this.availableObjectsNames );
    }

    /** Point of finishing for the inspector */
    private void finish()
    {
        this.setVisible( false );
    }

    /** Triggered when the user changes the name of the object */
    private void onEditObjectName()
    {
        if ( !this.beingBuilt ) {
            String newName = this.edObjectName.getText().trim();

            if ( !( this.getObj().getName().equals( newName  ) ) ) {
                this.visualEngine.execute( this.getObj().getPath() + " rename \"" + newName + "\"" );
                this.updateObjectBasicAttributes();
            }
        }
    }

    /** Triggered when the user schanges the parent */
    private void onChangeParent()
    {
        if ( !this.beingBuilt ) {
            String newParentName = (String) this.cbParent.getSelectedItem();

            if ( !( this.getObj().getParentObject().getName().equals( newParentName  ) ) ) {
                this.visualEngine.execute( this.getObj().getPath() + ".parent = " + newParentName );
                this.updateObjectBasicAttributes();
                this.finish();
            }
        }
    }

    /** Triggered when the user adds a new attribute */
    private void onAddAttribute()
    {
        String attrName = "attr" + ( this.getObj().getNumberOfAttributes()  + 1 );

        this.visualEngine.execute( this.getObj().getPath() + "." + attrName + " = 0" );
        final Attribute attr = this.getObj().lookUpAttribute( attrName );

        if ( attr != null ) {
            this.addAttributePanel( attr );
            this.pack();
        }
    }

    /** Triggered when the user adds a new method */
    private void onAddMethod()
    {
        String mthName = "mth" + ( this.getObj().getNumberOfMethods() + 1 );

        this.visualEngine.execute( this.getObj().getPath() + "." + mthName + " = {:}" );
        final ObjectBag.Slot slot = this.getObj().lookUpSlot( mthName );
        if ( slot != null ) {
            this.addMethodPanel( slot );
            this.pack();
        }
    }

    /** Triggered when the user modifies the name of an attribute */
    private void onRenameAttribute(JTextField edName, Attribute attr)
    {
        if ( !this.beingBuilt ) {
            String newName = edName.getText();

            if ( !( attr.getName().equals( newName ) ) ) {
                this.visualEngine.execute( getObj().getPath() + "." + attr.getName() + " rename \"" + newName + "\"" );
                edName.setText( attr.getName() );
            }
        }
    }

    private void onChangeContentsForAttribute(JComboBox cbContents, Attribute attr)
    {
        if ( !this.beingBuilt ) {
            String newContents = (String) cbContents.getSelectedItem();

            if ( !( attr.getReference().getNameOrValueAsString().equals( newContents ) ) ) {
                this.visualEngine.execute( this.getObj().getPath() + "." + attr.getName() + " = " + newContents );
            }
        }
    }

    /** Triggered when the user deletes an attribute */
    private void onDeleteAttribute(JPanel panel, Attribute attr)
    {
        if ( !this.beingBuilt ) {
            this.visualEngine.execute( this.getObj().getPath() + " erase \"" + attr.getName() + "\"" );
            this.pnlAttributes.remove( panel );
            Inspector.this.pack();
        }
    }

    /** Triggered when the user changes the name of a method */
    private void onRenameMethod(ObjectBag.Slot slot, JTextField edName)
    {
        if ( !this.beingBuilt ) {
            final String oldName = slot.getMethod().getName();
            final String newName = edName.getText();

            if ( !( oldName.equals( newName ) ) ) {
                this.visualEngine.execute(
                        this.getObj().getPath() + " renameMethod \"" + oldName + "\" \"" + newName + "\""  );
                edName.setText( slot.getMethod().getName() );
            }
        }
    }

    /** Triggered when the user changes the body of a method */
    private void onMethodBodyChanged(JTextField edContents, ObjectBag.Slot slot)
    {
        if ( !this.beingBuilt ) {
            final Method mth = slot.getMethod();
            final String mthName = mth.getName();
            final String newContents = edContents.getText().trim();

            if ( !( mth.getMethodBodyAsString().equals( newContents ) ) ) {
                this.visualEngine.execute( getObj().getPath() + "." + mthName + " = " + newContents );
            }
        }
    }

    /** Triggered when the user deletes a method */
    private void onDeleteMethod(JPanel panel, ObjectBag.Slot slot)
    {
        if ( !this.beingBuilt ) {
            final Method mth = slot.getMethod();
            this.visualEngine.execute( this.getObj().getPath() + " erase \"" + mth.getName() + "\"" );
            this.pnlMethods.remove( panel );
            this.pack();
        }
    }

    final private ObjectBag obj;
    final private String objPath;
    final private Runtime rt;
    final private ObjectParent objInheritanceRoot;
    private String[] availableObjectsNames;
    private Boolean beingBuilt;

    private JTextField edObjectName;
    private JComboBox cbParent;
    private JPanel pnlAction;
    private JPanel pnlAttributes;
    private JPanel pnlMethods;
    private ImageIcon iconCopy;
    private ImageIcon iconAddMethod;
    private ImageIcon iconAddAttribute;
    private ImageIcon iconDelete;
    private ImageIcon iconExecute;
    private VisualEngine visualEngine;
}
