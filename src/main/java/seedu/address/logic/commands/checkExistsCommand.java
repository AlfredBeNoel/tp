package seedu.address.logic.commands;

import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.person.Person;

import static java.util.Objects.requireNonNull;

public class checkExistsCommand extends Command{

    private final Person toAdd;

    public checkExistsCommand(Person person) {
        requireNonNull(person);
        toAdd = person;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {

        if (model.hasPerson(toAdd)) {
            String MESSAGE_SUCCESS = "Yes!! They Exist";
            return new CommandResult(String.format(MESSAGE_SUCCESS, Messages.format(toAdd)));
        } else {
            throw new CommandException("Non-Existing Member!");
        }
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof checkExistsCommand)) {
            return false;
        }

        checkExistsCommand otherAddCommand = (checkExistsCommand) other;
        return toAdd.equals(otherAddCommand.toAdd);
    }

    @Override
    public String toString() {
        return "";
    }
}