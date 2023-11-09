package seedu.address.logic.parser;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_EMAIL;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PHONE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TAG;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TELEGRAM;

import java.util.Set;
import java.util.stream.Stream;

import seedu.address.logic.commands.AddMemberCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.person.Member;
import seedu.address.model.person.fields.Email;
import seedu.address.model.person.fields.Name;
import seedu.address.model.person.fields.Phone;
import seedu.address.model.person.fields.Telegram;
import seedu.address.model.tag.Tag;

/**
 * Parses input arguments and creates a new AddMemberCommand object
 */
public class AddMemberCommandParser implements Parser<AddMemberCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the AddMemberCommand
     * and returns an AddMemberCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public AddMemberCommand parse(String args) throws ParseException {

        StringBuilder finalString = new StringBuilder();
        Boolean changed = false;
        String finalMessage = " is missing!\n";

        requireNonNull(args);
        ArgumentMultimap argMultimap =
                ArgumentTokenizer.tokenize(args, PREFIX_NAME, PREFIX_PHONE, PREFIX_EMAIL, PREFIX_TELEGRAM, PREFIX_TAG);

        if (argMultimap.getValue(PREFIX_NAME).isEmpty()) {
            finalString.append(Name.TYPE);
            changed = true;
        }

        if (argMultimap.getValue(PREFIX_PHONE).isEmpty()) {
            finalString.append(", " + Phone.TYPE);
            changed = true;
        }

        if (argMultimap.getValue(PREFIX_EMAIL).isEmpty()) {
            finalString.append(", " + Email.TYPE);
            changed = true;
        }

        if (argMultimap.getValue(PREFIX_TELEGRAM).isEmpty()) {
            finalString.append(", " + Telegram.TYPE);
            changed = true;
        }

        if (!arePrefixesPresent(argMultimap, PREFIX_NAME, PREFIX_PHONE, PREFIX_EMAIL, PREFIX_TELEGRAM)
                || !argMultimap.getPreamble().isEmpty()) {
            String messageFormat;
            messageFormat = String.format(AddMemberCommand.MESSAGE_USAGE);
            if (changed) {
                messageFormat = String.format(finalMessage + finalMessage + AddMemberCommand.MESSAGE_USAGE);

            }
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, messageFormat));
        }

        argMultimap.verifyNoDuplicatePrefixesFor(PREFIX_NAME, PREFIX_PHONE, PREFIX_EMAIL, PREFIX_TELEGRAM);

        Name name = ParserUtil.parseName(argMultimap.getValue(PREFIX_NAME).get());
        Phone phone = ParserUtil.parsePhone(argMultimap.getValue(PREFIX_PHONE).get());
        Email email = ParserUtil.parseEmail(argMultimap.getValue(PREFIX_EMAIL).get());
        Telegram telegram = ParserUtil.parseTelegram(argMultimap.getValue(PREFIX_TELEGRAM).get());
        Set<Tag> tagList = ParserUtil.parseTags(argMultimap.getAllValues(PREFIX_TAG));


        Member member = new Member(name, phone, email, telegram, tagList);

        return new AddMemberCommand(member);
    }

    /**
     * Returns true if none of the prefixes contains empty {@code Optional} values in the given
     * {@code ArgumentMultimap}.
     */
    private static boolean arePrefixesPresent(ArgumentMultimap argumentMultimap, Prefix... prefixes) {
        return Stream.of(prefixes).allMatch(prefix -> argumentMultimap.getValue(prefix).isPresent());
    }

}
