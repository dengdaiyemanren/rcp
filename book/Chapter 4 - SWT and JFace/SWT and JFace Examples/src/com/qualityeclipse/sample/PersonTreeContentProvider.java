package com.qualityeclipse.sample;

import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ITreeContentProvider;

public class PersonTreeContentProvider
	extends ArrayContentProvider
	implements ITreeContentProvider {

	public Object[] getChildren(Object parentElement) {
		Person person = (Person) parentElement;
		return person.children;
	}

	public Object getParent(Object element) {
		Person person = (Person) element;
		return person.parent;
	}

	public boolean hasChildren(Object element) {
		Person person = (Person) element;
		return person.children.length > 0;
	}
}
