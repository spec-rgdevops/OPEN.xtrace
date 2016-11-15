package org.spec.research.open.xtrace.dflt.impl.core.callables;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.spec.research.open.xtrace.api.core.AdditionalInformation;
import org.spec.research.open.xtrace.api.core.SubTrace;
import org.spec.research.open.xtrace.api.core.callables.Callable;
import org.spec.research.open.xtrace.api.core.callables.NestingCallable;
import org.spec.research.open.xtrace.dflt.impl.core.AbstractIdentifiableImpl;
import org.spec.research.open.xtrace.dflt.impl.core.SubTraceImpl;
import org.spec.research.open.xtrace.dflt.impl.core.TraceImpl;

/**
 * Default implementation of the {@link Callable} API element.
 * 
 * @author Alexander Wert, Christoph Heger
 *
 */
public abstract class AbstractCallableImpl extends AbstractIdentifiableImpl implements Callable, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3742066050942762287L;

	/**
	 * {@link AbstractNestingCallableImpl} instance that called this Callable.
	 */
	protected AbstractNestingCallableImpl parent;

	/**
	 * Entry timestamp [milliseconds].
	 */
	protected long entryTime = -1;

	/**
	 * List of label identifiers. Identifiers point to a repository in the
	 * containing trace.
	 */
	protected Optional<List<Integer>> labelIds = Optional.empty();

	/**
	 * List of additional information objects.
	 */
	protected Optional<Collection<AdditionalInformation>> additionInfos = Optional.empty();

	/**
	 * Containing SubTrace.
	 */
	protected SubTraceImpl containingSubTrace;
	
	/**
	 * Name of the executed thread.
	 */
	private Optional<String> threadName = Optional.empty();
	
	/**
	 * ID of the executed thread.
	 */
	private Optional<Long> threadID = Optional.empty();

	/**
	 * Default constructor for serialization. This constructor should not be
	 * used except for deserialization.
	 */
	public AbstractCallableImpl() {

	}

	/**
	 * Constructor. Adds the newly created {@link Callable} instance to the
	 * passed parent if the parent is not null!
	 * 
	 * @param parent
	 *            {@link AbstractNestingCallableImpl} that called this Callable
	 * @param containingSubTrace
	 *            the SubTrace containing this Callable
	 */
	public AbstractCallableImpl(AbstractNestingCallableImpl parent, SubTraceImpl containingSubTrace) {
		this.parent = parent;
		if (parent != null) {
			parent.addCallee(this);
		}
		this.containingSubTrace = containingSubTrace;
	}

	@Override
	public NestingCallable getParent() {
		return parent;
	}

	@Override
	public SubTrace getContainingSubTrace() {
		return containingSubTrace;
	}

	@Override
	public long getTimestamp() {
		return entryTime;
	}

	public void setTimestamp(long timestamp) {
		entryTime = timestamp;
	}

	@Override
	public Optional<List<String>> getLabels() {
		if (!labelIds.isPresent()) {
			return Optional.empty();
		}
		
		TraceImpl trace = ((TraceImpl) getContainingSubTrace().getContainingTrace());

		Optional<List<String>> resultList = labelIds.map(l -> l.stream().map(i -> trace.getStringConstant(i)).collect(Collectors.toList()));
		
		return Optional.of(Collections.unmodifiableList(resultList.get()));

	}

	/**
	 * Attaches a label to this {@link Callable}.
	 * 
	 * @param label
	 *            lable to add
	 */
	public void addLabel(String label) {

		if (!labelIds.isPresent()) {
			labelIds = Optional.of(new ArrayList<Integer>());
		}

		int hash = ((TraceImpl) containingSubTrace.getContainingTrace()).registerStringConstant(label);
		labelIds.get().add(hash);
	}

	@Override
	public Optional<Collection<AdditionalInformation>> getAdditionalInformation() {
		
		if (!additionInfos.isPresent()) {
			return Optional.empty();
		}
		
		return Optional.of(Collections.unmodifiableCollection(additionInfos.get()));
	}

	/**
	 * Attaches an additional information object to this {@link Callable}.
	 * 
	 * @param additionalInfo
	 *            additional information to attach
	 */
	public void addAdditionalInformation(AdditionalInformation additionalInfo) {
		if (!additionInfos.isPresent()) {
			additionInfos = Optional.of(new ArrayList<AdditionalInformation>());
		}
		additionInfos.get().add(additionalInfo);
	}

	@Override
	@SuppressWarnings("unchecked")
	public <T extends AdditionalInformation> Optional<Collection<T>> getAdditionalInformation(Class<T> type) {
		if (additionInfos.isPresent()) {

			List<T> result = new ArrayList<T>();
			for (AdditionalInformation aInfo : additionInfos.get()) {
				if (type.isAssignableFrom(aInfo.getClass())) {
					result.add((T) aInfo);
				}
			}
			return Optional.of(Collections.unmodifiableCollection(result));
		} else {
			return Optional.empty();
		}
	}
	
	@Override
	public Optional<String> getThreadName(){
		return this.threadName;
	}
	
	/**
	 * @param threadName
	 *            the name of the thread
	 */
	public void setThreadName(String threadName){
		this.threadName = Optional.ofNullable(threadName);
	}
	
	@Override
	public Optional<Long> getThreadID(){
		return this.threadID;
	}
	
	/**
	 * @param threadID
	 *            the ID of the thread
	 */
	public void setThreadID(long threadID){
		this.threadID = Optional.ofNullable(threadID);
	}

}
